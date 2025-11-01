-- 1st task
-- Make a query to retrieve attributes from the specified tables by applying filters on the specified conditions:
-- Н_ТИПЫ_ВЕДОМОСТЕЙ, Н_ВЕДОМОСТИ.
-- Select the atributes: Н_ТИПЫ_ВЕДОМОСТЕЙ.НАИМЕНОВАНИЕ, Н_ВЕДОМОСТИ.ЧЛВК_ИД.
--  Ведомость, Перезачет < 'Экзаменационный лист'
-- no data before 1998..

SELECT Н_ТИПЫ_ВЕДОМОСТЕЙ.НАИМЕНОВАНИЕ,
       Н_ВЕДОМОСТИ.ЧЛВК_ИД
FROM Н_ТИПЫ_ВЕДОМОСТЕЙ
INNER JOIN Н_ВЕДОМОСТИ 
    ON Н_ТИПЫ_ВЕДОМОСТЕЙ.ИД = Н_ВЕДОМОСТИ.ТВ_ИД -- ТВ_ИД Внешний ключ к таблице Н_ТИПЫ_ВЕДОМОСТЕЙ
WHERE Н_ТИПЫ_ВЕДОМОСТЕЙ.НАИМЕНОВАНИЕ < 'Экзаменационный лист'
  AND Н_ВЕДОМОСТИ.ДАТА < '1998-01-05';

-- INNER JOIN CONTROL
SELECT Н_ТИПЫ_ВЕДОМОСТЕЙ.НАИМЕНОВАНИЕ,
       Н_ВЕДОМОСТИ.ЧЛВК_ИД,
       Н_ВЕДОМОСТИ.ДАТА
FROM Н_ТИПЫ_ВЕДОМОСТЕЙ
INNER JOIN Н_ВЕДОМОСТИ
    ON Н_ТИПЫ_ВЕДОМОСТЕЙ.ИД = Н_ВЕДОМОСТИ.ТВ_ИД;


-- Name verification: Two pieces of data come before the examination list.
--1 - Ведомость
--3 - Перезачет

SELECT ИД, НАИМЕНОВАНИЕ
FROM Н_ТИПЫ_ВЕДОМОСТЕЙ
WHERE Н_ТИПЫ_ВЕДОМОСТЕЙ.НАИМЕНОВАНИЕ < 'Экзаменационный лист';


-- DATE CONTROL: No data before 1998
SELECT Н_ВЕДОМОСТИ.ДАТА
FROM Н_ВЕДОМОСТИ
WHERE Н_ВЕДОМОСТИ.ДАТА < '1998-01-05';

-- Inner join retrieves data that meets the conditions in both tables.


-- 2 Make a query to retrieve attributes from the specified tables by applying filters on the specified conditions:
-- Tables: Н_ЛЮДИ, Н_ОБУЧЕНИЯ, Н_УЧЕНИКИ.
-- Output the atributes: Н_ЛЮДИ.ИМЯ, Н_ОБУЧЕНИЯ.ЧЛВК_ИД, Н_УЧЕНИКИ.НАЧАЛО.
-- Фильтры: (AND), Н_ЛЮДИ.ОТЧЕСТВО < Сергеевич. b) Н_ОБУЧЕНИЯ.НЗК > 999080. c) Н_УЧЕНИКИ.ГРУППА = 3100.
-- Вид соединения: RIGHT JOIN.

SELECT Н_ЛЮДИ.ИМЯ,
       Н_ОБУЧЕНИЯ.ЧЛВК_ИД,
       Н_УЧЕНИКИ.НАЧАЛО
FROM Н_ЛЮДИ
RIGHT JOIN Н_ОБУЧЕНИЯ
    ON Н_ЛЮДИ.ИД = Н_ОБУЧЕНИЯ.ЧЛВК_ИД
RIGHT JOIN Н_УЧЕНИКИ
    ON Н_ОБУЧЕНИЯ.ЧЛВК_ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
WHERE Н_ЛЮДИ.ОТЧЕСТВО < 'Сергеевич'
  AND Н_ОБУЧЕНИЯ.НЗК > '999080'
  AND Н_УЧЕНИКИ.ГРУППА = '3100';
-- Therefore, the Н_ОБУЧЕНИЯ, Н_ЛЮДИ table acts as a "bridge" between the Н_УЧЕНИКИ table. The following diagram can be summarized:
-- Н_ЛЮДИ (ИД)
-- ↓ (FK: ЧЛВК_ИД)
-- Н_ОБУЧЕНИЯ (ЧЛВК_ИД, ВИД_ОБУЧ_ИД, НЗК, …)
-- ↓ (FK: ЧЛВК_ИД, ВИД_ОБУЧ_ИД)
-- Н_УЧЕНИКИ (ЧЛВК_ИД, ВИД_ОБУЧ_ИД, ГРУППА, НАЧАЛО, …)


SELECT ИД, ФАМИЛИЯ, ИМЯ, ОТЧЕСТВО
FROM Н_ЛЮДИ
WHERE Н_ЛЮДИ.ОТЧЕСТВО < 'Сергеевич';
SELECT ЧЛВК_ИД, НЗК, ВИД_ОБУЧ_ИД
FROM Н_ОБУЧЕНИЯ
ORDER BY НЗК DESC --(descending)
    LIMIT 100000;


SELECT ЧЛВК_ИД, НЗК, ВИД_ОБУЧ_ИД
FROM Н_ОБУЧЕНИЯ
WHERE Н_ОБУЧЕНИЯ.НЗК IS NULL -- > 999080 is NULL
    AND Н_ОБУЧЕНИЯ.ЧЛВК_ИД = '112809';

SELECT ИД, ГРУППА
FROM Н_УЧЕНИКИ
WHERE Н_УЧЕНИКИ.ГРУППА = '3100';

--FROM tablo1
--RIGHT JOIN tablo2 ON ...
--Table2, that is, the table on the right, is fully preserved in all cases. All rows in Table1 that do not match Table2 become NULL.


-- 3. Compose a query that answers the question whether there are any students in group 3102 without a TIN.
SELECT Н_ЛЮДИ.ИД,
       Н_ЛЮДИ.ФАМИЛИЯ,
       Н_ЛЮДИ.ИНН,
       Н_УЧЕНИКИ.ГРУППА
FROM Н_ЛЮДИ
JOIN Н_УЧЕНИКИ
    ON Н_ЛЮДИ.ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
WHERE Н_УЧЕНИКИ.ГРУППА = '3102'
  AND Н_ЛЮДИ.ИНН IS NULL;


-- Returns true or false indicating whether an INN number exists.
-- Returns true or false indicating existence.
-- SELECT 1 is specifically used in subqueries like IN EXISTS.
-- Instead of SELECT 1, you can also write SELECT * or SELECT 'anything', but using 1 is faster and more common,
-- because it only checks for existence, not the data content.
SELECT EXISTS (
    SELECT 1
    FROM Н_УЧЕНИКИ
             JOIN Н_ЛЮДИ ON Н_УЧЕНИКИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
    WHERE Н_УЧЕНИКИ.ГРУППА = '3102'
      AND (Н_ЛЮДИ.ИНН IS NULL OR Н_ЛЮДИ.ИНН = '')
) AS есть_ли_студенты_без_ИНН;





-- 4. Find groups in which there were more than 5 enrolled students in 2011 in the Department of Computer Science.
-- Use a table join to implement.
SELECT Н_УЧЕНИКИ.ГРУППА
FROM Н_УЧЕНИКИ
JOIN Н_ПЛАНЫ
    ON Н_УЧЕНИКИ.ПЛАН_ИД = Н_ПЛАНЫ.ИД
JOIN Н_ОТДЕЛЫ
    ON Н_ПЛАНЫ.ОТД_ИД = Н_ОТДЕЛЫ.ИД
WHERE Н_ОТДЕЛЫ.ИМЯ_В_ИМИН_ПАДЕЖЕ LIKE '%вычислительной техники%'
  AND Н_УЧЕНИКИ.НАЧАЛО < '2012-01-01'
  AND (Н_УЧЕНИКИ.КОНЕЦ IS NULL OR Н_УЧЕНИКИ.КОНЕЦ >= '2011-01-01')
GROUP BY Н_УЧЕНИКИ.ГРУППА
HAVING COUNT(*) > 5;

-- schema explanation:
-- Н_УЧЕНИКИ (ПЛАН_ИД, ГРУППА, НАЧАЛО, КОНЕЦ, …)
-- ↓ (FK: ПЛАН_ИД)
-- Н_ПЛАНЫ (ОТД_ИД, УЧЕБНЫЙ_ГОД, …)
-- ↓ (FK: ОТД_ИД)
-- Н_ОТДЕЛЫ (ИД, ИМЯ_В_ИМИН_ПАДЕЖЕ = 'кафедра вычислительной техники', …)

-- GROUP information is in the N_STUDENTS table.
-- To find how many students were enrolled in a group in 2011, use GROUP BY GROUP and COUNT(*).
-- "More than 5" → HAVING COUNT(*) > 5.

SELECT * FROM Н_ОТДЕЛЫ
WHERE ИМЯ_В_ИМИН_ПАДЕЖЕ LIKE '%вычислительной техники%';

SELECT ИД, КОРОТКОЕ_ИМЯ, ИМЯ_В_ИМИН_ПАДЕЖЕ
FROM Н_ОТДЕЛЫ
WHERE ИМЯ_В_ИМИН_ПАДЕЖЕ LIKE '%вычислительной техники%';

ИД  | КОРОТКОЕ_ИМЯ |       ИМЯ_В_ИМИН_ПАДЕЖЕ
-----+--------------+--------------------------------
 102 | ВТ           | кафедра вычислительной техники
(1 строка)


SELECT * FROM Н_УЧЕНИКИ
WHERE Н_УЧЕНИКИ.НАЧАЛО < '2012-01-01'
  AND (Н_УЧЕНИКИ.КОНЕЦ IS NULL OR Н_УЧЕНИКИ.КОНЕЦ >= '2011-01-01');

SELECT ИД, ИМЯ_В_ИМИН_ПАДЕЖЕ
FROM Н_ОТДЕЛЫ
WHERE Н_ОТДЕЛЫ.ИМЯ_В_ИМИН_ПАДЕЖЕ = 'кафедра вычислительной техники';


SELECT Н_ПЛАНЫ.ОТД_ИД
FROM Н_ПЛАНЫ
WHERE Н_ПЛАНЫ.ОТД_ИД = 102;

SELECT Н_ОТДЕЛЫ.ИМЯ_В_ИМИН_ПАДЕЖЕ
FROM Н_ОТДЕЛЫ
WHERE Н_ОТДЕЛЫ.ИМЯ_В_ИМИН_ПАДЕЖЕ = 'кафедра вычислительной техники';

SELECT ОТД_ИД FROM Н_ПЛАНЫ
WHERE ОТД_ИД = '102';


SELECT Н_ПЛАНЫ.ОТД_ИД  FROM Н_ПЛАНЫ;





--Display a table with the average age of students in all groups
--(Group, Average Age), where the average age is less than the minimum age in group 3100.
SELECT
    Н_УЧЕНИКИ.ГРУППА, -- grup
    ROUND(AVG(EXTRACT(YEAR FROM AGE(Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ))), 2) AS СРЕДНИЙ_ВОЗРАСТ -- average age
FROM
    Н_УЧЕНИКИ
JOIN
    Н_ЛЮДИ ON Н_УЧЕНИКИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
WHERE
    Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ IS NOT NULL
  AND Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ <= CURRENT_DATE 
GROUP BY
    Н_УЧЕНИКИ.ГРУППА
HAVING
    AVG(EXTRACT(YEAR FROM AGE(Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ))) <
    (
        SELECT MIN(EXTRACT(YEAR FROM AGE(Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ)))
        FROM Н_УЧЕНИКИ
        JOIN Н_ЛЮДИ
            ON Н_УЧЕНИКИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
        WHERE Н_УЧЕНИКИ.ГРУППА = '3100'
          AND Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ <= CURRENT_DATE
    );



SELECT l.ИД, l.ДАТА_РОЖДЕНИЯ,
       EXTRACT(YEAR FROM AGE(CURRENT_DATE, l.ДАТА_РОЖДЕНИЯ)) AS возраст
FROM Н_УЧЕНИКИ u
         JOIN Н_ЛЮДИ l ON u.ЧЛВК_ИД = l.ИД
WHERE u.ГРУППА = '3100'
  AND l.ДАТА_РОЖДЕНИЯ <= CURRENT_DATE
  AND l.ДАТА_РОЖДЕНИЯ IS NOT NULL;


-- AND l.ДАТА_РОЖДЕНИЯ <= CURRENT_DATE 
SELECT MIN(EXTRACT(YEAR FROM AGE(CURRENT_DATE, l.ДАТА_РОЖДЕНИЯ))) AS min_age_3100
FROM Н_УЧЕНИКИ u
         JOIN Н_ЛЮДИ l ON u.ЧЛВК_ИД = l.ИД
WHERE u.ГРУППА = '3100';



SELECT
    u.ГРУППА,
    ROUND(AVG(EXTRACT(YEAR FROM AGE(CURRENT_DATE, l.ДАТА_РОЖДЕНИЯ))),2) AS ср_возраст
FROM Н_УЧЕНИКИ u
         JOIN Н_ЛЮДИ l ON u.ЧЛВК_ИД = l.ИД
WHERE l.ДАТА_РОЖДЕНИЯ <= CURRENT_DATE
GROUP BY u.ГРУППА;



SELECT Н_ФОРМЫ_ОБУЧЕНИЯ.НАИМЕНОВАНИЕ
FROM Н_ФОРМЫ_ОБУЧЕНИЯ;


-- Get the list of students who were expelled exactly on the first of September 2012 from full-time or part-time education
-- (specialty: Software Engineering).
-- The result should include:
-- group number;
-- number, surname, first name and patronymic of the student;
-- order paragraph number;
-- To implement, use a subquery with EXISTS.

SELECT
  Н_УЧЕНИКИ.ГРУППА,
  Н_УЧЕНИКИ.ИД AS НомерСтудента,
  Н_ЛЮДИ.ФАМИЛИЯ,
  Н_ЛЮДИ.ИМЯ,
  Н_ЛЮДИ.ОТЧЕСТВО,
  Н_УЧЕНИКИ.П_ПРКОК_ИД AS НомерПунктаПриказа
FROM
  Н_УЧЕНИКИ
JOIN
  Н_ЛЮДИ ON Н_УЧЕНИКИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
WHERE Н_УЧЕНИКИ.КОНЕЦ  = '2012-09-01'
  AND Н_УЧЕНИКИ.ПРИЗНАК = 'отчисл'
  AND EXISTS (
    SELECT 1
    FROM Н_ОБУЧЕНИЯ
    JOIN Н_ВИДЫ_ОБУЧЕНИЯ ON Н_ОБУЧЕНИЯ.ВИД_ОБУЧ_ИД = Н_ВИДЫ_ОБУЧЕНИЯ.ИД
    JOIN Н_ПЛАНЫ ON Н_УЧЕНИКИ.ПЛАН_ИД = Н_ПЛАНЫ.ИД
    JOIN Н_НАПР_СПЕЦ ON Н_ПЛАНЫ.НАПС_ИД = Н_НАПР_СПЕЦ.ИД
    WHERE
        Н_ОБУЧЕНИЯ.ЧЛВК_ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
      AND Н_ВИДЫ_ОБУЧЕНИЯ.НАИМЕНОВАНИЕ IN ('Основное образование', 'Второе образование')
      AND Н_НАПР_СПЕЦ.НАИМЕНОВАНИЕ = 'Программная инженерия'
  );

--CAST(Students."END" AS DATE) = '2012-09-01'
--Since the END field is of type timestamp (date + time),
-- CAST is used to extract only the date portion.
--Thus, records with an exact date of September 1, 2012
SELECT *
FROM Н_ВИДЫ_ОБУЧЕНИЯ;
-- Второе образование
-- Основное образование


SELECT НАИМЕНОВАНИЕ
FROM Н_НАПР_СПЕЦ
WHERE Н_НАПР_СПЕЦ.НАИМЕНОВАНИЕ = 'Программная инженерия';

--No one graduated or left school on September 1, 2012.
SELECT COUNT(*)
FROM "Н_УЧЕНИКИ"
WHERE КОНЕЦ = '2012-09-01';




--7. QUERY
--Display a list of students with identical first names but different IDs.
SELECT Н_ЛЮДИ.ИД,
       Н_ЛЮДИ.ИМЯ
FROM Н_ЛЮДИ
WHERE Н_ЛЮДИ.ИМЯ IN (
    SELECT Н_ЛЮДИ.ИМЯ
    FROM Н_ЛЮДИ
    GROUP BY Н_ЛЮДИ.ИМЯ
    HAVING COUNT(*) > 1
)
ORDER BY Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ИД;

--Subquery
--GROUP BY Н_ЛЮДИ.ИМЯ:
--Gathers (groups) records with the same name together. So if there are 3 people named "Ahmet," they all become one group.

--HAVING COUNT(*) > 1:
--Selects only those groups containing more than one record. So people whose name appears 2 or more times remain.

--Lists all records matching the names of people whose names appear more than once in the inner query.
--So, for example, if the name Hamza appears 3 times, it brings all 3 people along with their id and name.
--Results are sorted by name and id.




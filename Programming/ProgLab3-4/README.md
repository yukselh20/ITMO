# Lab Assignment #3-4: Object-Oriented Modeling of a Literary Excerpt

This project focuses on applying Object-Oriented Programming (OOP) principles to model a scenario described in a literary text. The primary goal is to identify key characters and objects, define their states and behaviors, and implement this model as a dynamic simulation in Java.

## Assignment Description

Based on the provided literary excerpt, the task is to create an object model of the real or imagined world described in the text. Key characters and items must be identified, along with their inherent states and behaviors. Using this model, a Java program that simulates the events of the story is to be written.

### Assignment Variant (7849)

The object model is based on the following text:

> *ENGLISH: Every day, newspapers printed reports that Officer Svistulkina could not be found anywhere. Some newspapers began to mock the fact that the police themselves couldn't find the missing officer. One newspaper even published a caricature depicting an officer searching for himself with a lantern during the day. It all ended with various jokes and funny stories being printed about the missing officer, reaching a point where they wrote that Officer Svistulkina hadn't disappeared at all—rather, he couldn't be found because no such Svistulkina ever existed in the world.*


> *RUSSIAN: В газетах каждый день печатались сообщения о том, что милиционера Свистулькина нигде не могут найти. Некоторые газеты начали подсмеиваться над тем, что сама милиция не может отыскать пропавшего милиционера. В одной газете даже напечатали карикатуру с изображением милиционера, который днем с фонарем сам себя ищет. Кончилось все это тем, что по поводу пропавшего милиционера стали печатать разные шуточки и смешные рассказы и дошли до того, что написали, будто милиционер Свистулькин совсем не исчезал, а найти его не могут потому, что никакого Свистулькина вовсе на свете не было.*
---

## Object-Oriented Model Overview

The literary text was analyzed to abstract key entities and their interactions. The resulting model is built around four main actors:

1.  **Svistulkina**: The central character, whose state (missing, non-existent) is the main subject of the story.
2.  **Newspapers**: Entities that publish information. They are specialized into different types (reporting, joking, cartooning) based on the text.
3.  **Polices**: The entity responsible for investigating the disappearance.
4.  **Readers**: The public, who react to the news by discussing and speculating.

The interactions between these entities are defined through interfaces, representing actions like `Publishable`, `Discussable`, and `Investigateable`. The state of characters is managed using enums like `PersonStatus`.

## Project Structure and Key Components

The project is organized into packages for classes, enums, interfaces, and exceptions to ensure a modular and maintainable structure.

### Core Classes

*   **`abstract class Essence`**: A base abstract class for all entities, providing a `name` and overriding `equals()` and `hashCode()` for proper object comparison.

*   **`class Person extends Essence`**: Represents a person. It adds a `status` (e.g., `MISSING`, `ACTIVE`) managed by the `PersonStatus` enum.
    *   **`class Svistulkina extends Person`**: Represents the main character. Implements the `Disappearable` interface and has a method `checkExistence()` to dynamically update its status.
    *   **`class Polices extends Person`**: Represents the police force. Implements the `Investigable` interface to perform investigations.
    *   **`class Readers extends Person`**: Represents the public. Implements `Discussable` and `Speculatable` to react to the news.

*   **`class Newspapers extends Essence`**: A base class for newspapers. It implements the `Publishable` interface.
    *   **`class ReportNewspapers`**: A newspaper that prints factual reports.
    *   **`class CartoonNewspapers`**: A newspaper that can publish cartoons. Implements `Illustratable`, `Ridiculable`, and `Searchable`.
    *   **`class JokingNewspapers`**: A newspaper that publishes jokes and funny stories. Implements `Printable` and `Speculatable`. It can also throw a `HumorOverdoseException`.

### Interfaces (Behaviors)

Interfaces are used to define contracts for actions that different classes can perform:
*   `Discussable`: `void discuss(String topic)`
*   `Disappearable`: `void disappear()`
*   `Illustratable`: `void illustrate(String description)`
*   `Investigateable`: `void investigate(String caseDetails)`
*   `Printable`: `void printReport(String report)`, `void printJoke(String joke)`
*   `Publishable`: `void publish(String type, String content)`
*   `Ridiculable`: `void ridicule(String subject, String content)`
*   `Searchable`: `void search(String query)`
*   `Speculatable`: `void speculate(String subject, String hypothesis)`

### Enums (States)

Enums define a fixed set of constants for managing states and types:
*   **`DailyNewspaperTypes`**: `REPORT`, `CARTOON`, `JOKE`, `FUNNY_STORY`.
*   **`PersonStatus`**: `MISSING`, `FOUND`, `DOES_NOT_EXIST`, `ACTIVE`, `IDLE`.

### Custom Exceptions

Two custom exception classes were created to handle specific logical errors:
*   **`HumorOverdoseException`**: A `RuntimeException` thrown when `JokingNewspapers` publishes too many jokes.
*   **`NoneExistentEntityException`**: A checked `Exception` to handle cases where an action is attempted on an entity that is confirmed not to exist.

### UML Diagram

The complete object model, including class relationships, inheritance, and interface implementations, is visualized in the UML diagram.

**Link to UML Diagram:** [https://shorturl.at/3214u](https://shorturl.at/3214u)

## Execution Logic (`Main.java`)

The `main` method orchestrates a dynamic simulation of the story:
1.  **Initialization**: Officer Svistulkina is created with a randomly assigned initial status (`MISSING` or `DOES_NOT_EXIST`).
2.  **News Publication**: A `ReportNewspapers` instance publishes a random article about the officer.
3.  **Jokes and Stories**: A `JokingNewspapers` instance randomly creates either a joke or a funny story.
4.  **Reader Reaction**: A `Readers` instance randomly either discusses the news or speculates about Svistulkina's fate.
5.  **Police Investigation**: A `Polices` instance investigates the case, with the outcome being random.
6.  **Status Update**: The simulation concludes by dynamically updating Svistulkina's existence status based on a final random check.

## Execution Example

Below is a sample output from running the program:

```
Svistulkina is initially: DOES_NOT_EXIST
Daily News published a FUNNY_STORY: Officer Svistulkina is still missing.
Humor Daily added a joke: Why can't Svistulkina find himself? Because he doesn't exist!
Jane Doe speculates about Svistulkina: might just be a ghost!
Detective Smith is investigating: The case of the missing Svistulkina.
The investigation found no trace of Svistulkina.
Svistulkina is still missing but might exist.
```

## Conclusion

This project demonstrates a well-structured application of OOP principles, focusing on abstraction, inheritance, polymorphism, and encapsulation. The modular class structure, combined with interfaces and enums, promotes code reuse, scalability, and effective state management. Simplifying components enhances readability and simplifies code maintenance. Custom exceptions and dynamic simulations improve reliability and testing. The clear project structure facilitates collaboration, making it suitable for real-world applications and providing a solid foundation for future enhancements.

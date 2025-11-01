import task1_re
import unittest
# pattern = 8-{O


class TestTask1(unittest.TestCase):
    def test_no_emotions(self):
        text = "Hello my name is Hamza:)"
        result = 0
        return (self.assertEqual(result, task1_re.search_emotions(text)))
    
    def test_emotion_with_blank(self):
        text = "Today, wather 8-{ Ois so good" 
        result = 0
        return (self.assertEqual(result, task1_re.search_emotions(text)))
    
    def test_multiple_emotions(self):
        text = "Studying 8-{Oat ITMO is not -{O8-{O8-{O8-{O stressful(!)"
        result = 4
        return (self.assertEqual(result, task1_re.search_emotions(text)))
    
    def test_one_emotion(self):
        text = "8-{O" 
        result = 1
        return (self.assertEqual(result, task1_re.search_emotions(text)))
    
    def test_splited_emotions(self):
        text = "8-8-{O8-8-{O{O{8-{O8-{O8-{OO8-{O"
        result = 6
        return (self.assertEqual(result, task1_re.search_emotions(text)))
    

if __name__ == '__main__':
    unittest.main()
        


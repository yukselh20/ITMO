import task2_re
import unittest


class TaskTest2(unittest.TestCase):
    def test_less_syllables(self):
        text = "Evening at the window./Another day lived./ Without you"
        result = "Not Haiku!"
        return (self.assertEqual(result, task2_re.find_pattern(text)))
    
    def test_less_lines(self):
        text = "This is/not a haiku poem."
        result = "The number of lines should be 3"
        return (self.assertEqual(result, task2_re.find_pattern(text)))
    
    def test_traditional_haiku(self):
        text = "An old silent pond/A frog jumps into the pond/Splash! Silence again."
        result = "Not Haiku!"
        return (self.assertEqual(result, task2_re.find_pattern(text)))
    
    def test_modern_haiku(self):
        text = "In the twilight rain/These brilliant-hued hibiscus/A lovely sunset."
        result = "Not Haiku!"
        return (self.assertEqual(result, task2_re.find_pattern(text)))
    
    def test_personal_reflection(self):
        text = "First autumn morning/The mirror I stare into/Shows my father's face"
        result = "Not Haiku!"
        return (self.assertEqual(result, task2_re.find_pattern(text)))


if __name__ == "__main__":
    unittest.main()

from Regex import multi_vowel_word
import unittest
class TestMultiVowelWord(unittest.TestCase):
    def test_basic(self):
        testcase = "Queen"
        excepted = ["Queen"]
        self.assertListEqual(multi_vowel_word(testcase), excepted)

    def test_empty_string(self):
        testcase = ""
        excepted = []
        self.assertListEqual(multi_vowel_word(testcase), excepted)

unittest.main()
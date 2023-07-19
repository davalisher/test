package uz.pdp.test.regex;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Pattern pattern;
        Matcher matcher;
        String searchString;
        String text;
        text = "Thisa is a string";
        searchString = "\\Bis\\B";
        pattern = Pattern.compile(searchString);
        matcher = pattern.matcher(text);

        while (matcher.find()) {
            System.out.println("found: " + matcher.group());
        }

    }
}

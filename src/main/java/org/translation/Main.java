package org.translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    private static final String QUIT = "quit";
    private static final String IS = " is ";

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        Translator translator = new JSONTranslator();
        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String country = promptForCountry(translator, scanner);
            if (country == null || QUIT.equalsIgnoreCase(country)) {
                break;
            }

            String language = promptForLanguage(translator, country);
            if (language == null || QUIT.equalsIgnoreCase(language)) {
                break;
            }

            String translation = translator.translate(country, language);
            if (translation == null) {
                System.out.println("Translation not available.");
            } else {
                CountryCodeConverter countryConverter = new CountryCodeConverter();
                LanguageCodeConverter languageConverter = new LanguageCodeConverter();

                String countryName = countryConverter.fromCountryCode(country);
                String languageName = languageConverter.fromLanguageCode(language);
                System.out.println(countryName + " in " + languageName + IS + translation);
            }

            System.out.println("Press enter to continue or type 'quit' to exit.");
            if (QUIT.equalsIgnoreCase(scanner.nextLine().trim())) {
                break;
            }
        }
    }

    // Prompt the user to select a country from the sorted list of country names
    private static String promptForCountry(Translator translator, Scanner scanner) {
        List<String> countryCodes = translator.getCountries();
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        List<String> countryNames = new ArrayList<>();

        for (String countryCode : countryCodes) {
            String countryName = countryCodeConverter.fromCountryCode(countryCode);
            if (countryName != null) {
                countryNames.add(countryName);
            }
        }

        countryNames.sort(String::compareToIgnoreCase);

        for (String countryName : countryNames) {
            System.out.println(countryName);
        }

        System.out.println("Select a country from above or type 'quit' to exit:");
        String countryName = scanner.nextLine().trim();

        if (QUIT.equalsIgnoreCase(countryName)) {
            return QUIT;
        }

        return countryCodeConverter.fromCountry(countryName);
    }

    // Prompt the user to select a language from the sorted list of language names
    private static String promptForLanguage(Translator translator, String countryCode) {
        Scanner scanner = new Scanner(System.in);
        List<String> languageCodes = translator.getCountryLanguages(countryCode);
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

        if (languageCodes == null || languageCodes.isEmpty()) {
            System.out.println("No languages available for this country: " + countryCode);
            return QUIT;
        }

        List<String> languageNames = new ArrayList<>();
        for (String languageCode : languageCodes) {
            String languageName = languageCodeConverter.fromLanguageCode(languageCode);
            if (languageName != null) {
                languageNames.add(languageName);
            }
        }

        languageNames.sort(String::compareToIgnoreCase);

        for (String languageName : languageNames) {
            System.out.println(languageName);
        }

        System.out.println("Select a language from above or type 'quit' to exit:");
        String languageName = scanner.nextLine().trim();

        if (QUIT.equalsIgnoreCase(languageName)) {
            return QUIT;
        }

        return languageCodeConverter.fromLanguage(languageName);
    }
}

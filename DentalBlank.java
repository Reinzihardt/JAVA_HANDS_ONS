import java.util.Scanner;

/**
 * AURELIA DENTAL CLINIC MANAGEMENT SYSTEM
 */

public class DentalBlank {

    static final int MAX = 50;
    static final int CANCELLED = -999;
    static final Scanner sc = new Scanner(System.in);
    static boolean inputEnded = false;

    // Patient parallel arrays
    static String[] patientIds = new String[MAX];
    static String[] patientNames = new String[MAX];
    static int[] patientAges = new int[MAX];
    static String[] patientContacts = new String[MAX];
    static String[] patientConcerns = new String[MAX];
    static int patientCount = 0;

    // Appointment parallel arrays
    static String[] apptIds = new String[MAX];
    static String[] apptPatientIds = new String[MAX];
    static String[] apptDates = new String[MAX];
    static String[] apptTimes = new String[MAX];
    static String[] apptDentists = new String[MAX];
    static String[] apptProcedures = new String[MAX];
    static String[] apptStatus = new String[MAX];
    static int apptCount = 0;
    
    static String[] dentists = { "Dr. Lance Barroquillo", "Dr. Elias Cruz", "Dr. Hannah Reyes" };

    // Dental concerns the patient can choose from (last one = Others)
    static String[] concerns = {
            "Toothache", "Cleaning / Checkup", "Cavity / Tooth decay",
            "Broken / Cracked tooth", "Wisdom tooth / Loose tooth",
            "Braces / Alignment", "Teeth whitening", "Gum problem", "Others"
    };

    // Suggested procedure (index in procedures[]) for each concern above
    static int[] concernSuggestion = { 0, 1, 2, 7, 3, 6, 5, 0, 0 };

    static String[] procedures = {
            "Consultation", "Cleaning", "Filling", "Extraction",
            "Root Canal", "Whitening", "Braces Check", "Crown / Bridge"
    };

    static int[] procedureFees = { 500, 800, 1500, 2000, 4500, 3500, 700, 8000 };

    static String[] timeSlots = {
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "13:00",
            "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30"
    };

    // ===================== MAIN =====================
    public static void main(String[] args) {
        loadSampleData();

        int choice;
        do {
            showMenu();
            choice = readChoice();

            if (choice == 1) {
                addPatient();
            } else if (choice == 2) {
                searchPatient();
            } else if (choice == 3) {
                displayPatients();
            } else if (choice == 4) {
                bookAppointment();
            } else if (choice == 5) {
                displaySchedule();
            } else if (choice == 6) {
                cancelAppointment();
            } else if (choice == 7) {
                dailySummary();
            } else if (choice == 8) {
                System.out.println();
                System.out.println("Thank you for using Aurelia Dental Clinic Management System.");
            } else {
                System.out.println("Invalid choice. Please enter 1-8.");
            }

            if (choice != 8) {
                pause();
            }
        } while (choice != 8);
    }

    static void showMenu() {
        System.out.println();
        System.out.println("====================================");
        System.out.println("  DENTAL CLINIC MANAGEMENT SYSTEM");
        System.out.println("====================================");
        System.out.println();
        System.out.println("1. Add Patient");
        System.out.println("2. Search Patient");
        System.out.println("3. Display Patients");
        System.out.println("4. Book Appointment");
        System.out.println("5. Display Schedule");
        System.out.println("6. Cancel Appointment");
        System.out.println("7. Daily Summary");
        System.out.println("8. Exit");
        System.out.println();
        System.out.print("Enter your choice: ");
    }

    // ===================== INPUT HELPERS =====================

    // Reads one line safely (no crash if the input stream is closed)
    static String readLine() {
        if (!sc.hasNextLine()) {
            inputEnded = true;
            return "";
        }
        return sc.nextLine().trim();
    }

    static int readChoice() {
        String line = readLine();
        if (inputEnded) {
            return 8;
        }
        return parseNumber(line);
    }

    // Digits-only text -> int. Returns -1 for anything else.
    static int parseNumber(String text) {
        if (text.length() == 0 || text.length() > 6) {
            return -1;
        }
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) < '0' || text.charAt(i) > '9') {
                return -1;
            }
        }
        return Integer.parseInt(text);
    }

    // Shows a prompt and reads a line. Returns null if the user types "cancel".
    static String ask(String prompt) {
        System.out.print(prompt);
        String text = readLine();
        if (inputEnded) {
            return null;
        }
        if (text.equalsIgnoreCase("cancel")) {
            System.out.println("Action cancelled.");
            return null;
        }
        return text;
    }

    // Asks again and again until the user enters a number from min to max
    static int askInt(String prompt, int min, int max, String errorMessage) {
        while (true) {
            String text = ask(prompt);
            if (text == null) {
                return CANCELLED;
            }
            int value = parseNumber(text);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println(errorMessage);
        }
    }

    static boolean askYesNo(String prompt) {
        while (true) {
            String text = ask(prompt + " (Y/N): ");
            if (text == null) {
                return false;
            }
            if (text.equalsIgnoreCase("y")) {
                return true;
            }
            if (text.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Please type Y or N.");
        }
    }

    static String askDate(String prompt) {
        while (true) {
            String text = ask(prompt);
            if (text == null || isValidDate(text)) {
                return text;
            }
            System.out.println("Invalid date. Use a real date in YYYY-MM-DD format (example: 2026-11-25).");
        }
    }

    // Shows the list of concerns. If the user picks "Others", they type their own.
    static String askConcern() {
        System.out.println("Dental Concerns:");
        for (int i = 0; i < concerns.length; i++) {
            System.out.println((i + 1) + ". " + concerns[i]);
        }
        int c = askInt("Enter concern number (1-" + concerns.length + "): ", 1, concerns.length,
                "Invalid choice. Please enter 1-" + concerns.length + ".");
        if (c == CANCELLED) {
            return null;
        }
        if (c < concerns.length) {
            return concerns[c - 1];
        }

        while (true) {
            String text = ask("Please describe the concern (1-30 characters): ");
            if (text == null) {
                return null;
            }
            if (text.length() >= 1 && text.length() <= 30) {
                return "Others: " + text;
            }
            System.out.println("Invalid description. Enter 1-30 characters.");
        }
    }

    // Procedure number (0-based) suggested for a patient's concern
    static int suggestedProcedure(String concern) {
        for (int i = 0; i < concerns.length; i++) {
            if (concerns[i].equals(concern)) {
                return concernSuggestion[i];
            }
        }
        return 0; // "Others: ..." -> Consultation
    }

    // ===================== FEATURES =====================
    static void addPatient() {
        System.out.println();
        System.out.println("--- ADD PATIENT ---");

        if (patientCount >= MAX) {
            System.out.println("Cannot add record. Patient list is full (maximum 50).");
            return;
        }

        // Name
        String name;
        while (true) {
            name = ask("Enter Patient Name (or type 'cancel'): ");
            if (name == null) {
                return;
            }
            name = toTitleCase(name);
            if (!isValidName(name)) {
                System.out.println("Invalid name. Use 2-30 characters: letters, spaces, . - ' only.");
            } else if (findPatientByName(name) != -1) {
                System.out.println("A patient with that name already exists.");
            } else {
                break;
            }
        }

        // Age
        int age = askInt("Enter Age (1-120): ", 1, 120, "Invalid age. Enter a whole number from 1 to 120.");
        if (age == CANCELLED) {
            return;
        }

        // Contact
        String contact;
        while (true) {
            contact = ask("Enter Contact Number: ");
            if (contact == null) {
                return;
            }
            if (isValidContact(contact)) {
                break;
            }
            System.out.println("Invalid contact. Enter 7-15 digits (you may use + - ( ) and spaces).");
        }

        // Concern (chosen from a list)
        String concern = askConcern();
        if (concern == null) {
            return;
        }

        patientIds[patientCount] = generateId("P", patientCount + 1);
        patientNames[patientCount] = name;
        patientAges[patientCount] = age;
        patientContacts[patientCount] = contact;
        patientConcerns[patientCount] = concern;
        patientCount++;

        System.out.println();
        System.out.println("Patient record added successfully.");
        System.out.println("Assigned Patient ID: " + patientIds[patientCount - 1]);
    }

    static void searchPatient() {
        System.out.println();
        System.out.println("--- SEARCH PATIENT ---");

        String query;
        while (true) {
            query = ask("Enter patient name or ID to search: ");
            if (query == null) {
                return;
            }
            if (query.length() > 0) {
                break;
            }
            System.out.println("Search text cannot be empty.");
        }
        query = query.toLowerCase();

        boolean found = false;
        for (int i = 0; i < patientCount; i++) {
            if (patientNames[i].toLowerCase().contains(query) || patientIds[i].toLowerCase().equals(query)) {
                if (!found) {
                    System.out.println();
                    System.out.println("Patient Found!");
                }
                found = true;
                System.out.println();
                System.out.println("ID: " + patientIds[i]);
                System.out.println("Name: " + patientNames[i]);
                System.out.println("Age: " + patientAges[i]);
                System.out.println("Contact: " + patientContacts[i]);
                System.out.println("Concern: " + patientConcerns[i]);
            }
        }

        if (!found) {
            System.out.println("Patient not found.");
        }
    }

    static void displayPatients() {
        System.out.println();
        System.out.println("PATIENT LIST");
        System.out.println("------------------------------------------------------------------------------------------");
        if (patientCount == 0) {
            System.out.println("No patient records found.");
        } else {
            System.out.printf("%-6s %-30s %-5s %-16s %s%n", "ID", "NAME", "AGE", "CONTACT", "CONCERN");
            for (int i = 0; i < patientCount; i++) {
                System.out.printf("%-6s %-30s %-5d %-16s %s%n",
                        patientIds[i], patientNames[i], patientAges[i], patientContacts[i], patientConcerns[i]);
            }
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.println("Total patients: " + patientCount);
        }
    }

    static void bookAppointment() {
        System.out.println();
        System.out.println("--- BOOK APPOINTMENT ---");

        if (apptCount >= MAX) {
            System.out.println("Cannot add record. Appointment list is full (maximum 50).");
            return;
        }
        if (patientCount == 0) {
            System.out.println("No patient records found. Add a patient first.");
            return;
        }

        // 1. Patient
        int pIndex = askPatient();
        if (pIndex == -1) {
            return;
        }
        System.out.println("Selected patient: " + patientNames[pIndex] + " (" + patientIds[pIndex] + ")");

        // 2. Date
        String date = askDate("Enter Date (YYYY-MM-DD): ");
        if (date == null) {
            return;
        }

        // 3. Dentist
        System.out.println("Dentists:");
        for (int i = 0; i < dentists.length; i++) {
            System.out.println((i + 1) + ". " + dentists[i]);
        }
        int d = askInt("Enter dentist number (1-3): ", 1, dentists.length, "Invalid dentist. Choose 1-3.");
        if (d == CANCELLED) {
            return;
        }
        String dentist = dentists[d - 1];

        // 4. Time (booked slots are marked)
        System.out.println("Time slots for " + dentist + " on " + date + ":");
        int open = 0;
        for (int i = 0; i < timeSlots.length; i++) {
            boolean booked = hasConflict(dentist, date, timeSlots[i]);
            if (!booked) {
                open++;
            }
            System.out.printf("%2d. %s %-9s", i + 1, timeSlots[i], booked ? "(booked)" : "");
            if ((i + 1) % 4 == 0 || i == timeSlots.length - 1) {
                System.out.println();
            }
        }
        if (open == 0) {
            System.out.println(dentist + " is fully booked on that date. Try another date or dentist.");
            return;
        }

        String time;
        while (true) {
            String text = ask("Enter slot number (1-14) or time (example 09:00): ");
            if (text == null) {
                return;
            }
            time = toTimeSlot(text);
            if (time == null) {
                System.out.println("Invalid time. Clinic hours are 09:00-16:30 (30-minute slots).");
            } else if (hasConflict(dentist, date, time)) {
                System.out.println("Appointment conflict. That dentist is already booked at " + time + ".");
            } else if (patientBusy(patientIds[pIndex], date, time)) {
                System.out.println("This patient already has an appointment at " + time + ".");
            } else {
                break;
            }
        }

        // 5. Procedure
        int suggested = suggestedProcedure(patientConcerns[pIndex]);
        System.out.println("Procedures (patient concern: " + patientConcerns[pIndex] + "):");
        for (int i = 0; i < procedures.length; i++) {
            String note = (i == suggested) ? "  <- suggested" : "";
            System.out.println((i + 1) + ". " + procedures[i] + "  (PHP " + procedureFees[i] + ")" + note);
        }
        int p = askInt("Enter procedure number (1-8): ", 1, procedures.length, "Invalid procedure. Choose 1-8.");
        if (p == CANCELLED) {
            return;
        }
        String procedure = procedures[p - 1];

        // 6. Confirm and save
        System.out.println();
        System.out.println("Patient   : " + patientNames[pIndex]);
        System.out.println("Date/Time : " + date + " " + time);
        System.out.println("Dentist   : " + dentist);
        System.out.println("Procedure : " + procedure);
        System.out.println("Fee       : PHP " + getFee(procedure));
        if (!askYesNo("Confirm booking?")) {
            System.out.println("Booking was not saved.");
            return;
        }

        addAppointment(generateId("A", apptCount + 1), patientIds[pIndex], date, time, dentist, procedure);
        System.out.println("Appointment booked successfully.");
        System.out.println("Appointment ID: " + apptIds[apptCount - 1]);
    }

    static void displaySchedule() {
        System.out.println();
        System.out.println("--- DISPLAY SCHEDULE ---");
        String date = askDate("Enter date (YYYY-MM-DD): ");
        if (date == null) {
            return;
        }

        System.out.println();
        System.out.println("SCHEDULE FOR " + date);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.printf("%-6s %-6s %-26s %-18s %-14s %-8s %s%n",
                "ID", "TIME", "PATIENT", "DENTIST", "PROCEDURE", "FEE", "STATUS");

        int shown = 0;
        // Loop through time slots first so the list is sorted by time
        for (int s = 0; s < timeSlots.length; s++) {
            for (int i = 0; i < apptCount; i++) {
                if (apptDates[i].equals(date) && apptTimes[i].equals(timeSlots[s])) {
                    int pIndex = findPatientById(apptPatientIds[i]);
                    System.out.printf("%-6s %-6s %-26s %-18s %-14s %-8d %s%n",
                            apptIds[i], apptTimes[i], patientNames[pIndex], apptDentists[i],
                            apptProcedures[i], getFee(apptProcedures[i]), apptStatus[i]);
                    shown++;
                }
            }
        }

        if (shown == 0) {
            System.out.println("No appointments found for that date.");
        }
        System.out.println("---------------------------------------------------------------------------------------");
    }

    static void cancelAppointment() {
        System.out.println();
        System.out.println("--- CANCEL APPOINTMENT ---");

        int index = -1;
        while (index == -1) {
            String id = ask("Enter Appointment ID (example A001): ");
            if (id == null) {
                return;
            }
            for (int i = 0; i < apptCount; i++) {
                if (apptIds[i].equalsIgnoreCase(id)) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                System.out.println("Appointment not found. Check the ID and try again.");
            }
        }

        if (apptStatus[index].equals("Cancelled")) {
            System.out.println("That appointment is already cancelled.");
            return;
        }

        System.out.println(apptIds[index] + ": " + patientNames[findPatientById(apptPatientIds[index])]
                + ", " + apptDates[index] + " " + apptTimes[index] + ", " + apptDentists[index]);
        if (!askYesNo("Cancel this appointment?")) {
            System.out.println("Appointment was not cancelled.");
            return;
        }

        apptStatus[index] = "Cancelled";
        System.out.println("Appointment cancelled.");
        System.out.println("The " + apptTimes[index] + " slot of " + apptDentists[index] + " can now be rebooked.");
    }

    static void dailySummary() {
        System.out.println();
        System.out.println("--- DAILY SUMMARY ---");
        String date = askDate("Enter date (YYYY-MM-DD): ");
        if (date == null) {
            return;
        }

        int scheduled = 0;
        int cancelled = 0;
        int totalFees = 0;
        int[] dentistLoad = new int[dentists.length];

        for (int i = 0; i < apptCount; i++) {
            if (!apptDates[i].equals(date)) {
                continue;
            }
            if (apptStatus[i].equals("Cancelled")) {
                cancelled++;
            } else {
                scheduled++;
                totalFees = totalFees + getFee(apptProcedures[i]);
                for (int d = 0; d < dentists.length; d++) {
                    if (dentists[d].equals(apptDentists[i])) {
                        dentistLoad[d]++;
                    }
                }
            }
        }

        System.out.println();
        System.out.println("SUMMARY FOR " + date);
        System.out.println("------------------------------------");
        System.out.println("Active appointments : " + scheduled);
        System.out.println("Cancelled           : " + cancelled);
        System.out.println("Estimated fees      : PHP " + totalFees);
        System.out.println();
        System.out.println("Load per dentist:");
        for (int i = 0; i < dentists.length; i++) {
            System.out.println("  " + dentists[i] + " - " + dentistLoad[i]);
        }
        if (scheduled == 0) {
            System.out.println();
            System.out.println("No active appointments found for that date.");
        }
    }

    // ===================== SEARCH HELPERS =====================

    // Asks until exactly one patient is identified. Returns -1 if cancelled.
    static int askPatient() {
        while (true) {
            String query = ask("Enter Patient Name or ID (or type 'cancel'): ");
            if (query == null) {
                return -1;
            }
            String q = query.toLowerCase();
            if (q.length() == 0) {
                System.out.println("Input cannot be empty.");
                continue;
            }

            int matches = 0;
            int first = -1;
            for (int i = 0; i < patientCount; i++) {
                if (patientIds[i].toLowerCase().equals(q) || patientNames[i].toLowerCase().equals(q)) {
                    return i; // exact ID or full name
                }
                if (patientNames[i].toLowerCase().contains(q)) {
                    matches++;
                    if (first == -1) {
                        first = i;
                    }
                }
            }

            if (matches == 1) {
                return first;
            } else if (matches == 0) {
                System.out.println("Patient not found. Try again or type 'cancel'.");
            } else {
                System.out.println("More than one patient matches. Please enter the Patient ID:");
                for (int i = 0; i < patientCount; i++) {
                    if (patientNames[i].toLowerCase().contains(q)) {
                        System.out.println("  " + patientIds[i] + " - " + patientNames[i]);
                    }
                }
            }
        }
    }

    // Is this dentist already booked at that date and time?
    static boolean hasConflict(String dentist, String date, String time) {
        for (int i = 0; i < apptCount; i++) {
            if (apptStatus[i].equals("Scheduled") && apptDentists[i].equals(dentist)
                    && apptDates[i].equals(date) && apptTimes[i].equals(time)) {
                return true;
            }
        }
        return false;
    }

    // Does this patient already have an appointment at that date and time?
    static boolean patientBusy(String patientId, String date, String time) {
        for (int i = 0; i < apptCount; i++) {
            if (apptStatus[i].equals("Scheduled") && apptPatientIds[i].equals(patientId)
                    && apptDates[i].equals(date) && apptTimes[i].equals(time)) {
                return true;
            }
        }
        return false;
    }

    static int findPatientByName(String name) {
        for (int i = 0; i < patientCount; i++) {
            if (patientNames[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    static int findPatientById(String id) {
        for (int i = 0; i < patientCount; i++) {
            if (patientIds[i].equals(id)) {
                return i;
            }
        }
        return -1;
    }

    static int getFee(String procedure) {
        for (int i = 0; i < procedures.length; i++) {
            if (procedures[i].equals(procedure)) {
                return procedureFees[i];
            }
        }
        return 0;
    }

    // ===================== VALIDATION / STRING HELPERS =====================
    static boolean isValidName(String name) {
        if (name.length() < 2 || name.length() > 30) {
            return false;
        }
        boolean hasLetter = false;
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            boolean letter = (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
            boolean extra = ch == ' ' || ch == '.' || ch == '-' || ch == '\'';
            if (letter) {
                hasLetter = true;
            } else if (!extra) {
                return false;
            }
        }
        return hasLetter;
    }

    static boolean isValidContact(String contact) {
        int digits = 0;
        for (int i = 0; i < contact.length(); i++) {
            char ch = contact.charAt(i);
            if (ch >= '0' && ch <= '9') {
                digits++;
            } else if (ch != '+' && ch != '-' && ch != ' ' && ch != '(' && ch != ')') {
                return false;
            }
        }
        return digits >= 7 && digits <= 15 && contact.length() <= 20;
    }

    // Checks the YYYY-MM-DD format AND that the date really exists (no Feb 30, etc.)
    static boolean isValidDate(String date) {
        if (date.length() != 10 || date.charAt(4) != '-' || date.charAt(7) != '-') {
            return false;
        }
        for (int i = 0; i < date.length(); i++) {
            if (i != 4 && i != 7 && (date.charAt(i) < '0' || date.charAt(i) > '9')) {
                return false;
            }
        }
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));
        if (year < 2000 || year > 2100 || month < 1 || month > 12) {
            return false;
        }

        int maxDay = 31;
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            maxDay = 30;
        } else if (month == 2) {
            boolean leap = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
            maxDay = leap ? 29 : 28;
        }
        return day >= 1 && day <= maxDay;
    }

    // "3" -> third slot, "9:00" -> "09:00". Returns null if not a valid slot.
    static String toTimeSlot(String text) {
        int number = parseNumber(text);
        if (number >= 1 && number <= timeSlots.length) {
            return timeSlots[number - 1];
        }
        if (text.length() == 4 && text.charAt(1) == ':') {
            text = "0" + text;
        }
        for (int i = 0; i < timeSlots.length; i++) {
            if (timeSlots[i].equals(text)) {
                return text;
            }
        }
        return null;
    }

    // "  juan   DELA cruz " -> "Juan Dela Cruz"
    static String toTitleCase(String text) {
        String[] words = text.trim().split("\\s+");
        String result = "";
        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > 0) {
                result = result + words[i].substring(0, 1).toUpperCase()
                        + words[i].substring(1).toLowerCase() + " ";
            }
        }
        return result.trim();
    }

    static String generateId(String prefix, int number) {
        if (number < 10) {
            return prefix + "00" + number;
        } else if (number < 100) {
            return prefix + "0" + number;
        }
        return prefix + number;
    }

    static void pause() {
        if (!inputEnded) {
            System.out.println();
            System.out.print("Press Enter to continue...");
            readLine();
        }
    }

    // ===================== SAMPLE DATA =====================
    static void loadSampleData() {
        addSeedPatient("P001", "Maria Clara Santos", 28, "0917-123-4567", "Toothache");
        addSeedPatient("P002", "Juan Dela Cruz", 35, "0918-234-5678", "Cleaning / Checkup");
        addSeedPatient("P003", "Angelica Reyes", 22, "0919-345-6789", "Braces / Alignment");
        addSeedPatient("P004", "Mark Anthony Lim", 45, "0920-456-7890", "Broken / Cracked tooth");
        addSeedPatient("P005", "Sofia Garcia", 31, "0921-567-8901", "Teeth whitening");
        addSeedPatient("P006", "Roberto Mendoza", 62, "0922-678-9012", "Wisdom tooth / Loose tooth");

        addAppointment("A001", "P001", "2026-11-25", "09:00", "Dr. Lance Barroquillo", "Filling");
        addAppointment("A002", "P002", "2026-11-25", "09:30", "Dr. Elias Cruz", "Cleaning");
        addAppointment("A003", "P003", "2026-11-25", "10:00", "Dr. Hannah Reyes", "Braces Check");
        addAppointment("A004", "P004", "2026-11-25", "11:00", "Dr. Lance Barroquillo", "Crown / Bridge");
        addAppointment("A005", "P005", "2026-11-26", "13:00", "Dr. Elias Cruz", "Whitening");
        addAppointment("A006", "P006", "2026-11-26", "14:00", "Dr. Hannah Reyes", "Extraction");
        addAppointment("A007", "P001", "2026-11-26", "10:00", "Dr. Lance Barroquillo", "Consultation");
        apptStatus[apptCount - 1] = "Cancelled"; // sample of a cancelled appointment
    }

    static void addSeedPatient(String id, String name, int age, String contact, String concern) {
        patientIds[patientCount] = id;
        patientNames[patientCount] = name;
        patientAges[patientCount] = age;
        patientContacts[patientCount] = contact;
        patientConcerns[patientCount] = concern;
        patientCount++;
    }

    static void addAppointment(String id, String patientId, String date, String time,
            String dentist, String procedure) {
        apptIds[apptCount] = id;
        apptPatientIds[apptCount] = patientId;
        apptDates[apptCount] = date;
        apptTimes[apptCount] = time;
        apptDentists[apptCount] = dentist;
        apptProcedures[apptCount] = procedure;
        apptStatus[apptCount] = "Scheduled";
        apptCount++;
    }
}

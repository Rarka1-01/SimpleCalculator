import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public enum TypeNumber{
        Arabic, Roman
    }

    public static int convertDigitToArabic(String digit){

        switch (digit){
            case "I": return 1;
            case "V": return 5;
            case "X": return 10;
            case "L": return 50;
            case "C": return 100;
        }

        return -1;
    }

    public static int convertToArabNum(String roman_number) {
        int arabic_number = 0;
        int i = 0;
        ArrayList<String> rom_alf = new ArrayList<>();
        rom_alf.add(0, "I");
        rom_alf.add(1, "V");
        rom_alf.add(2, "X");
        rom_alf.add(3, "L");
        rom_alf.add(4, "C");
        var r = roman_number.split("");
        while(i < r.length - 1){
            if(rom_alf.indexOf(r[i]) < rom_alf.indexOf(r[i+1]))
                arabic_number -= convertDigitToArabic(r[i]);
            else
                arabic_number += convertDigitToArabic(r[i]);
            i++;
        }
        arabic_number += convertDigitToArabic(r[i]);

        return arabic_number;
    }

    public static String createNSubstring(String substring, int n){
        String string = "";
        for(int i = 0; i < n; i++)
            string += substring;
        return string;
    }

    public static String convertToRomanDigit(int digit, int pos){
        String[] rom_alf = {"I", "V", "X", "L", "C"};
        switch (digit) {
            case 1: return rom_alf[pos - 1];
            case 2: return createNSubstring(rom_alf[pos - 1], 2);
            case 3: return createNSubstring(rom_alf[pos - 1], 3);
            case 4: return rom_alf[pos - 1] + rom_alf[pos];
            case 5: return rom_alf[pos];
            case 6: return rom_alf[pos] + rom_alf[pos - 1];
            case 7: return rom_alf[pos] + createNSubstring(rom_alf[pos - 1], 2);
            case 8: return rom_alf[pos] + createNSubstring(rom_alf[pos - 1], 3);
            case 9: return rom_alf[pos - 1] + rom_alf[pos + 1];
            case 0: return "";
        }
        return "-1";
    }

    public static String convertToRomanNum(int arabic_number) {
        String roman_num = "";
        int o = 1;
        int i = 1;

        while(arabic_number != 0) {
            int tmp = arabic_number % 10;
            roman_num = convertToRomanDigit(tmp, i) + roman_num;
            i += 2;
            arabic_number = arabic_number / 10;
        }

        return roman_num;
    }

    public static int calcWhithOper(int first, int second, String type_operation){

        switch (type_operation){
            case "+": return first + second;
            case "-": return first - second;
            case "*": return first * second;
            case "/": return first / second;
        }

        return -1;
    }

    public static String calculate(String first_number, String second_number, TypeNumber type, String type_operation) throws IOException {
        int result = 0;
        String res = "";

        if(type == TypeNumber.Arabic) {
            result = calcWhithOper(Integer.parseInt(first_number), Integer.parseInt(second_number), type_operation);
            res = Integer.toString(result);
        }
        else {
            result = calcWhithOper(convertToArabNum(first_number), convertToArabNum(second_number), type_operation);
            if(result < 1)
                throw new IOException("Unexist roman number");
            else
                res = convertToRomanNum(result);
        }

        return res;
    }

    public static boolean correctValue(String number, TypeNumber type){
        if(type == TypeNumber.Arabic) {
            if (Integer.parseInt(number) <= 10 && Integer.parseInt(number) > 0)
                return true;
            else
                return false;
        }
        else {
            int num = convertToArabNum(number);
            if (num <= 10 && num > 0)
                return true;
            else
                return false;
        }

    }

    public static boolean isRomanNumber(String number){
        var n = number.split("");
        for (String i : n) {
            if (!i.equals("I") && !i.equals("V") && !i.equals("X")
                    && !i.equals("L") && !i.equals("C"))
                return false;
        }

        return true;
    }

    public static TypeNumber correctTypeNumbersAndType(String first_number, String second_number) throws IOException {

        TypeNumber type;

        if(isRomanNumber(first_number) == isRomanNumber(second_number)) {
            if (isRomanNumber(first_number))
                type = TypeNumber.Roman;
            else
                type = TypeNumber.Arabic;
        }
        else
            throw new IOException("Incorrect type number");

        return type;
    }

    public static String getOperation(String input) throws IOException {
        if(input.contains("+"))
            return  "+";
        else if(input.contains("-"))
            return  "-";
        else if(input.contains("*"))
            return  "*";
        else if(input.contains("/"))
            return  "/";
        else
            throw new IOException("Undefined operation");
    }

    public static void isCorrectExpr(String input) throws IOException {
        var expr = input.split("");
        boolean oper = false;

        for(String i: expr){
            if(i.equals("+") || i.equals("-") || i.equals("*") || i.equals("/"))
                if(oper)
                    throw new IOException("new Exeption");
                else
                    oper = true;
        }
    }

    public static String calc(String input){
        String output = "";
        try{
            isCorrectExpr(input);
            String type_operation = getOperation(input);
            String first_number = input.substring(0, input.indexOf(type_operation));
            String second_number = input.substring(input.indexOf(type_operation)+1);

            TypeNumber type_numbers = correctTypeNumbersAndType(first_number, second_number);

            if(!correctValue(first_number, type_numbers) || !correctValue(second_number, type_numbers))
                throw new IOException("Incorrect number value");

            output = calculate(first_number, second_number, type_numbers, type_operation);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return output;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        System.out.println(calc(input));
    }
}
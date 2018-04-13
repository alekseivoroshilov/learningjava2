package tail9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.io.Console;

public class Tail {
    static void tailOfFiles(String[] args){
        ArrayList<String> files = new ArrayList<String>(); //то, куда кидаю перечень имен файлов с текстом
        Boolean c = false;
        Boolean n = false;
        Boolean outputFileExists = false;
        Integer cNum = 0;
        Integer nNum = 0;
        String oName = "output.txt";
        Pattern pattern = Pattern.compile("(([a-zA-z0-9|_|-]+).(txt|doc))");
        Matcher m;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-c": {
                    c = true;
                    i++;
                    cNum = Integer.parseInt(args[i]);
                    break;
                }
                case "-n": {
                    n = true;
                    i++;
                    nNum = Integer.parseInt(args[i]);
                    break;
                }
                case "-o": {
                    outputFileExists = true;
                    i++;
                    oName = args[i];
                    break;
                }
            }
            if (!args[i].equals(oName)) {
                m = pattern.matcher(args[i]);
                if (m.matches()) files.add(args[i]);
            }
        }
        if (c && n) throw new IllegalArgumentException();
        ArrayList<String> lines = new ArrayList<String>();
        if (files.isEmpty()){
            System.out.println("There is no file to get tail from it. Write something, please.");
            //Console cl = System.console(); отдельно подумаю, как реализовать консоль. это более практично
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
                String s;
                do {
                    s = br.readLine();
                    lines.add(s);
                } while (!s.equals("exit"));
            } catch (Exception e) {
                System.out.println(e);
            }
            lines.remove(lines.size() - 1);
            System.out.println("Alright! You wrote this:" + lines);
        }

        ArrayList<String> lineList;
        StringBuilder sb;
        System.out.println(files);
        if (files.size() != 0){
            for (int i = 0; i < files.size(); i++) {
                if (n) lineList = readLines(files.get(i), nNum);
                else if (c) lineList = readSymbols(files.get(i), cNum);
                else lineList = readLines(files.get(i), 10);

                if (outputFileExists) {
                    write(oName, lineList);
                }
                else {
                    for (String line : lineList) {
                        System.out.print(line + System.lineSeparator());
                    }
                }
            }
        }
        else
        {
            if (n) lineList = readLines(lines, nNum);
            else if (c) {System.out.println(lines); lineList = readSymbols(lines, cNum); System.out.println(lineList);}
            else
                lineList = readLines(lines, 10);
            if (outputFileExists) {
                write(oName, lineList);
            }
            else {
                for (String line : lineList) {
                    System.out.print(line + System.lineSeparator());
                }
            }
        }

    }

    static private ArrayList<String> readLines(String fileName, Integer nNum) {
        ArrayList<String> lineList = new ArrayList<>();
        lineList.add(fileName + "\\n");
        //ArrayList<String> returnList = new ArrayList<>();
        //наткнулся на try-with-resources
        //надо выбрать кодировку, иначе кириллица будет кракозябная
        try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"))){
            //чтение построчно
            String s;
            int j = 0;
            while ((s = in.readLine()) != null && (j < nNum)) {
                j++;
                lineList.add(s);
            }
            while ((s = in.readLine()) != null){
                lineList.remove(0);
                lineList.add(s);
            }


        }
        catch (IOException ex) {
            System.out.println(ex.getMessage()); //должно выводить сообщение
        }

        return lineList;
    }
    static private ArrayList<String> readLines(ArrayList<String> lines, Integer nNum) {
        ArrayList<String> result = new ArrayList<String>();
        result.addAll(lines.subList(lines.size() - nNum, lines.size() - 1));
        return result;
    }
    static private ArrayList<String> readSymbols(String fileName, Integer cNum) {
        ArrayList<String> lineList = new ArrayList<>();
        lineList.add(fileName + "\\n");
        StringBuilder sb= new StringBuilder();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"))){
            // чтение посимвольно
            int symbol;
            int j = 0;
            char c;
            while ((symbol = in.read()) != -1 && (j < cNum)) {
                c = (char) symbol;
                sb.append(c);
                j++;
            }
            while ((symbol = in.read()) != -1){
                c = (char) symbol;
                sb.append(c);
                sb.deleteCharAt(0);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        Collections.addAll(lineList, sb.toString().split("\\n"));
        return lineList;
    }
    static private ArrayList<String> readSymbols(ArrayList<String> lines, Integer cNum) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = lines.size() - 1; i >= 0; i--){
            cNum -= lines.get(i).length();
            if (cNum <= 0){
                if (cNum == 0) break;
                String lastLine = lines.get(i);
                String substring = lastLine.substring(lastLine.length() + cNum, lastLine.length());
                result.add(substring);
                break;
            } else result.add(lines.get(i));
        }
        Collections.reverse(result);
        return result;
    }

    static void write(String fileName, ArrayList<String> lines) {
        String text = lines.toString();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));){
            for (String line: lines){
                bw.write(System.lineSeparator() + line);
            }

        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
        /*File file = new File(fileName);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                bw.write(text);
            }
        } catch (IOException e) { //от меня просто железно потребовалось ловить IOException
            System.out.println("Something is wrong with writing");
        }
    }*/
    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException(file.getName());
        }
    }
}

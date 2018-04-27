package tail9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

class Tail {
    static void tailOfFiles(String[] args) {
        ArrayList<String> files = new ArrayList<String>(); //то, куда кидаю перечень имен файлов с текстом
        boolean c = false;
        boolean n = false;
        boolean outputFileExists = false;
        int cNum = 0;
        int nNum = 0;
        String oName = "output.txt";
        //Pattern pattern = Pattern.compile("(([a-zA-z0-9|_|-]+).(txt|doc))");
        //Matcher m;

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
                default: if (!args[i].equals(oName)) files.add(args[i]);

            }
            //if (!args[i].equals(oName)) files.add(args[i]);
                //m = pattern.matcher(args[i]);
                //if (m.matches())
        }
        try (PrintWriter writer = new PrintWriter(oName);) {
            writer.print(""); // очищаем файл, перезаписав поверх пустой текст
        } catch (Exception e) {
            System.err.println("Error in file cleaning: " + e.getMessage());
        }

        if (c && n) throw new IllegalArgumentException();
        ArrayList<String> lines = new ArrayList<String>();
        if (files.isEmpty()) {
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
        }

        ArrayList<String> lineList;
        StringBuilder sb;
        System.out.println(files);
        if (files.size() != 0) {
            for (int i = 0; i < files.size(); i++) {
                if (n) lineList = readLines(files.get(i), nNum);
                else if (c) lineList = readSymbols(files.get(i), cNum);
                else lineList = readLines(files.get(i), 10);

                if (outputFileExists) {
                    write(oName, lineList);
                } else {
                    for (String line : lineList) {
                        System.out.print(line + System.lineSeparator());
                    }
                }
            }
        } else {
            if (n) lineList = readLines(lines, nNum);
            else if (c) {
                lineList = readSymbols(lines, cNum);
            } else
                lineList = readLines(lines, 10);
            if (outputFileExists) {
                write(oName, lineList);
            } else {
                for (String line : lineList) {
                    System.out.print(line + System.lineSeparator());
                }
            }
        }

    }

    static private ArrayList<String> readLines(String fileName, int nNum) {
        ArrayList<String> lineList = new ArrayList<>();
        //наткнулся на try-with-resources
        //надо выбрать кодировку, иначе кириллица будет кракозябная
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {
            lineList.add(System.lineSeparator() + "[" + fileName + "]" + System.lineSeparator());
            //чтение построчно
            String s;
            int j = 0;
            while ((s = in.readLine()) != null && (j < nNum)) {
                j++;
                lineList.add(s);
            }
            while ((s = in.readLine()) != null) {
                lineList.remove(1);
                lineList.add(s);
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage()); //должно выводить сообщение
        }
        return lineList;
    }

    static private ArrayList<String> readLines(ArrayList<String> lines, int nNum) {
        ArrayList<String> result = new ArrayList<String>();
        result.addAll(lines.subList(lines.size() - nNum, lines.size() - 1));
        return result;
    }

    static private ArrayList<String> readSymbols(String fileName, int cNum) {
        ArrayList<String> lineList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {
            // чтение посимвольно
            int symbol;
            int j = 0;
            char c;
            while ((symbol = in.read()) != -1 && (j < cNum)) {
                c = (char) symbol;
                sb.append(c);
                j++;
            }
            while ((symbol = in.read()) != -1) {
                c = (char) symbol;
                sb.append(c);
                sb.deleteCharAt(0);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        Collections.addAll(lineList, sb.toString().split("\\n"));
        lineList.add(System.lineSeparator() + "[" + fileName + "]" + System.lineSeparator());
        return lineList;
    }

    static private ArrayList<String> readSymbols(ArrayList<String> lines, int cNum) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = lines.size() - 1; i >= 0; i--) {
            cNum -= lines.get(i).length();
            if (cNum <= 0) {
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

    static private void write(String fileName, ArrayList<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));) {
            for (String line : lines) {
                bw.write(line + System.lineSeparator());
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

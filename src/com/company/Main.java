package com.company;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Double t;
    private static Double C;
    private static Double W;
    private static Double V;

    public static void main(String[] args) throws IOException {
        List<Double> n = new LinkedList<>();
        List<Double> r = new LinkedList<>();
        List<Double> v = new LinkedList<>();
        List<Double> w = new LinkedList<>();
        List<Double> alpha = new LinkedList<>();
        List<Double> beta = new LinkedList<>();
        String fileContent = readFile();
        if (fileContent.equals("")) {
            return;
        }
        if (!parseFileContent(fileContent, n, r, v, w, alpha, beta)) {
            return;
        }
        if (searchOfResult(n, r, v, w, alpha, beta)) {
            writeStringToFile(prepareResultFileContent(n, r, v, w, alpha, beta));
        }
        System.out.println();
    }

    private static Double calcC(double ri, double alphaI, double betaI) {
        return alphaI * Math.pow(-t / Math.log(ri), betaI);
    }

    private static Double calcGOne(List<Double> r, List<Double> n, List<Double> alpha, List<Double> beta) {
        assert r.size() == n.size();
        Double result = 0.;
        for (int i = 0; i < r.size(); i++) {
            result += calcC(r.get(i), alpha.get(i), beta.get(i)) * (n.get(i) + Math.exp(n.get(i) / 4));
        }
        result -= C;
        return result;
    }

    private static Double calcGTwo(List<Double> n, List<Double> w) {
        Double result = 0.;
        for (int i = 0; i < n.size(); i++) {
            result += w.get(i) * n.get(i) * Math.exp(n.get(i) / 4);
        }
        result -= W;
        return result;
    }

    private static double calcGThree(List<Double> n, List<Double> v) {
        Double result = 0.;
        for (int i = 0; i < n.size(); i++) {
            result += v.get(i) * Math.pow(n.get(i), 2);
        }
        result -= V;
        return result;
    }

    private static double calcRs(List<Double> r, List<Double> n) {
        assert r.size() == n.size();
        Double result = 1.;
        for (int i = 0; i < r.size(); i++) {
            result *= 1 - Math.pow(1 - r.get(i), n.get(i));
        }
        return result;
    }

    private static String readFile() throws IOException {
        String path = new File("").getCanonicalPath() + File.separator + "input.csv";
        StringBuilder result = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                result.append(scanner.nextLine());
                result.append('\n');
            }
        } catch (FileNotFoundException e) {
            System.out.println("Не могу найти входной файл. Назовите файл \"input.csv\" и положите его в папку с программой.");
            System.out.println("Формат заполнения файла: n;r;v;w;C;V;W;t;alpha;beta");
            System.out.println("Более детально про формат CSV вы можете прочитать в интернете (вики достаточно)");
            System.out.println("Сейчас попытаюсь сгенерировать файл автоматически");
            writeStringToFile(prepareInputFileContent());
        }
        return result.toString();
    }

    private static boolean parseFileContent(String content, List<Double> n, List<Double> r, List<Double> v, List<Double> w, List<Double> alpha, List<Double> beta) {
        List<String> strings = Arrays.asList(content.split("(\r\n|\r|\n)"));
        String[] lineContent;
        try {
            for (String string : strings) {
                lineContent = string.replace(" ", "").replace(',', '.').split(";");
                n.add(Double.valueOf(lineContent[0]));
                r.add(Double.valueOf(lineContent[1]));
                v.add(Double.valueOf(lineContent[2]));
                w.add(Double.valueOf(lineContent[3]));
                C = Double.valueOf(lineContent[4]);
                V = Double.valueOf(lineContent[5]);
                W = Double.valueOf(lineContent[6]);
                t = Double.valueOf(lineContent[7]);
                alpha.add(Double.valueOf(lineContent[8]));
                beta.add(Double.valueOf(lineContent[9]));
            }
        } catch (NumberFormatException e) {
            System.out.println("Во входном файле содержится не число. Сорян, не могу продолжить работу");
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("В каждой строке входного файла должно быть по 10 цифр, даже если из ряда в ряд они одинаковые!");
            System.out.println("Знаю, что это не очень, но таково требование");
            System.out.println("Формат заполнения файла: n,r,v,w,C,V,W,t,alpha,beta");
            return false;
        }
        return true;
    }

    private static void writeStringToFile(String str) throws IOException {
        String path = new File("").getCanonicalPath() + File.separator + "input.csv";
        if (!new File(path).createNewFile()) {
            System.out.println("Не получилось автоматически сгенерировать файл. Попробуйте это сделать самостоятельно.");
        }
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(path)))) {
            writer.write(str);
        }
        System.out.println("Входной файл автоматически сгенерирован. Вы можете вручную его редактировать");
    }

    private static String prepareInputFileContent() {
        //n,r,v,w,C,V,W,t,alpha,beta
        String result = "";
        result += String.format("1;0,5;4;9;533;289;483;1000;0,611360;1,5%n");
        result += String.format("2;0,5;5;7;533;289;483;1000;4,032464;1,5%n");
        result += String.format("3;0,5;3;5;533;289;483;1000;3,578225;1,5%n");
        result += String.format("4;0,5;2;9;533;289;483;1000;3,654303;1,5%n");
        result += String.format("5;0,5;3;9;533;289;483;1000;1,163718;1,5%n");
        result += String.format("6;0,5;4;10;533;289;483;1000;2,966955;1,5%n");
        result += String.format("7;0,5;1;6;533;289;483;1000;2,045865;1,5%n");
        result += String.format("8;0,5;1;5;533;289;483;1000;2,649522;1,5%n");
        result += String.format("9;0,5;4;8;533;289;483;1000;1,982908;1,5%n");
        result += String.format("10;0,5;4;6;533;289;483;1000;3,516724;1,5%n");
        return result;
    }

    private static boolean searchOfResult(List<Double> n, List<Double> r, List<Double> v, List<Double> w, List<Double> alpha, List<Double> beta) {
        Double g1;
        Double g2;
        Double g3;
        Double rs;
        Double maxRs = Double.MIN_VALUE;
        List<Double> inputN = new LinkedList<>(n);
        List<Double> bestN = null;
        Integer maxN = 5;
        n.clear();
        for (int i = 0; i < r.size(); i++) {
            n.add(0.);
        }
        for (int i = 0; i < maxN + 1; i++) {
            for (int j = 1; j < n.size(); j++) {
                n.set(j, n.get(j) + 1);
                g1 = calcGOne(r, n, alpha, beta);
                g2 = calcGTwo(n, w);
                g3 = calcGThree(n, v);
                rs = calcRs(r, n);
                if (maxRs < rs && g1 <= 0 && g2 <= 0 && g3 <= 0) {
                    maxRs = rs;
                    bestN = new LinkedList<>(n);
                }
            }
        }
        n.clear();
        if (bestN == null) {
            n.addAll(inputN);
            System.out.println("Не смог подобрать результат лучше, чем входные параметры.");
            return false;
        } else {
            n.addAll(bestN);
            System.out.println("Результат найден. Он будет записан в файл \"result.csv\" и находиться возле программы");
            return true;
        }
    }

    private static String prepareResultFileContent(List<Double> n, List<Double> r, List<Double> v, List<Double> w, List<Double> alpha, List<Double> beta) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("№;n;r;v;w;alpha;beta;V;C;W;T%n"));
        for (int i = 0; i < n.size(); i++) {
            result.append(String.format("%d;%f;%f;%f;%f;%f;%f;%f;%f;%f;%f%n",
                    i + 1,
                    n.get(i),
                    r.get(i),
                    v.get(i),
                    w.get(i),
                    alpha.get(i),
                    beta.get(i),
                    V,
                    C,
                    W,
                    t));
        }
        return result.toString();
    }
}

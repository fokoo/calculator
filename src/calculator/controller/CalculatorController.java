package calculator.controller;

import calculator.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/*
 Autor: Foko Fotso, Serge
 */


public class CalculatorController {

    @FXML
    private Label operationLabel;

    @FXML
    private Label resultLabel;

    private String operation;

    private String lastResult;

    private ArrayList<String> historyList = new ArrayList<>();

    public void OnMouseClicked(MouseEvent mouseEvent) {
        Button bt = (Button) mouseEvent.getSource();
        String expression = bt.getText();

        initValue();
        if(isNumber(expression)) {
            setNumber(expression);
        }else if(expression.equals("=")){
            setResult();
        }else if(isOperator(expression)){
            setOperator(expression);
        }else{
            switch (expression) {
                case "ANS":
                    useLastResult();
                    break;
                case "CLEAR":
                    clear();
                    break;
                case "DELETE":
                    delete();
                    break;
                case "HIST":
                    openHistoryWindow();
                    break;
            }
        }
    }

    private void openHistoryWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/calculator/resources/history.fxml"));
            Parent root = loader.load();
            //Stage historyStage = Main.getHistoryStage();
            Main.getHistoryStage().setScene(new Scene(root));

            HistoryController historyController = loader.getController();
            historyController.fillListView(historyList);

            Main.getHistoryStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setOperator(String expression) {
        if(operation.length()==0){
            return;
        }
        if(operation.charAt(operation.length()-1) == ' '){
            delete();
        }
        expression = " "+expression+" ";
        addToOperation(expression);  // is operator
        operationLabel.setText(operation);
    }

    private void setResult() {
        String result;
        final String ERROR = "NO DIVISION ERROR BY 0 !";
        if(operation.length()>0 && operation.charAt(operation.length()-1)==' '){
            delete();
        }
        result = evaluate(operation);
        resultLabel.setText(result);
        addToHistory(operation + "  =  " + result);

        if(!result.equals(ERROR)) {
            lastResult = result;
        }
        operation="";
    }

    private void addToHistory(String s) {
        //System.out.println(s);
        historyList.add(s);
        //System.out.println(historyList.contains(s));
    }

    private void setNumber(String expression) {
       /* if(isNumber(String.valueOf(operation.charAt(operation.length()-2)))
                && operation.charAt(operation.length()-1)==' '){
            return;
        }*/
        addToOperation(expression);
        operationLabel.setText(operation);
    }

    private void initValue() {
        if(operation==null){
            operation="";
        }
        if(lastResult==null){
            lastResult="0.0";
        }
         /* if(operation!=null && operation.equals("")){
            clear();
            operation = (lastResult==null)?null:lastResult;
            resultLabel.setText(lastResult);
        }*/
    }

    private void useLastResult() {
        int size = operation.length();
       /* if(size==0 || operation.charAt(size-1)==' '){
            addToOperation(lastResult+ " * ");
        }else if(isNumber(String.valueOf(operation.charAt(size-1)))){
            lastResult=" * "+lastResult+" * ";
            addToOperation(lastResult);
        }*/
        if(size==0 || operation.charAt(size-1)==' '){
            setNumber(lastResult);
            setOperator("*");
        }else if(isNumber(String.valueOf(operation.charAt(size-1)))){
            setOperator("*");
            setNumber(lastResult);
            setOperator("*");
        }
    }

    private void delete() {
       if(operation.equals("")){
            return;
       }
       int size = operation.length();
       if(operation.charAt(size-1)==' '){
           operation = operation.substring(0,size-3);
       }else{
           operation = operation.substring(0,size-1);
       }
       operationLabel.setText(operation);
    }

    private void clear() {
        operation = "";
        operationLabel.setText(" ");
    }

    private boolean isNumber(String expression) {
        boolean bol = false;
        switch (expression) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "0":
            case ".":
                bol=true;
                break;
        }
        return bol;
    }

    private boolean isOperator(String expression) {
        boolean bol = false;
        switch (expression) {
            case "+":
            case "-":
            case "*":
            case "/":
                bol=true;
                break;
        }
        return bol;
    }

    private void addToOperation(String exp) {
        operation = operation+exp;
    }

    private String evaluate(String operation) {
        String[] operationArray = operation.split(" ");
        ArrayList<String> operationList = new ArrayList<String>(Arrays.asList(operationArray));
        reduceOpertion(operationList, "*");
        reduceOpertion(operationList, "/");
        reduceOpertion(operationList, "+");
        reduceOpertion(operationList, "-");
        //System.out.println("end size: " + operationList.size());
        operationList.removeIf(n-> (n.equals(" ")));
        /*for (String str : operationList) {
            System.out.println("-> "+ str);
        }*/
        return operationList.get(0);
    }

    private void reduceOpertion(ArrayList<String> operationList, String s) {
        int index = -2;
        double newEntry = 0;
        while(operationList.contains(s)) {
            index = operationList.indexOf(s);
            if (s == "-") {
                newEntry = Double.valueOf(operationList.get(index - 1)) - Double.valueOf(operationList.get(index + 1));
            } else if (s == "+") {
                newEntry = Double.valueOf(operationList.get(index - 1)) + Double.valueOf(operationList.get(index + 1));
            } else if (s == "*") {
                newEntry = Double.valueOf(operationList.get(index - 1)) * Double.valueOf(operationList.get(index + 1));
            } else if (s == "/" && Double.valueOf(operationList.get(index + 1)) != 0.0) {
                newEntry = Double.valueOf(operationList.get(index - 1)) / Double.valueOf(operationList.get(index + 1));
            } else if (s == "/" && Double.valueOf(operationList.get(index + 1)) == 0.0) {
                operationList.set(0, "NO DIVISION ERROR BY 0 !");
                break;
            }
            operationList.set(index - 1, "x");
            operationList.set(index + 1, "x");
            operationList.set(index, String.valueOf(newEntry));
            operationList.removeIf(n -> (n.equals("x")));
        }
    }


}
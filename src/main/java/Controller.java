import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;

import java.io.*;


public class Controller {
    @FXML
    private TextField N;
    @FXML
    private TextField Q;
    @FXML
    private TextField maxSimulationTime;
    @FXML
    private TextField minArrivalTime;
    @FXML
    private TextField maxArrivalTime;
    @FXML
    private TextField minServiceTime;
    @FXML
    private TextField maxServiceTime;
    @FXML
    public TextArea displayTextArea;
    @FXML
    private Button startSimulation;
    private int valueOfN;
    private int valueOfQ;
    private int valueOfMaxSimulationTime;
    private int valueOfMinArrivalTime;
    private int valueOfMaxArrivalTime;
    private int valueOfMinServiceTime;
    private int valueOfMaxServiceTime;

    public int getValueOfN() {
        return valueOfN;
    }

    public void setValueOfN(int valueOfN) {
        this.valueOfN = valueOfN;
    }

    public int getValueOfQ() {
        return valueOfQ;
    }

    public void setValueOfQ(int valueOfQ) {
        this.valueOfQ = valueOfQ;
    }

    public int getValueOfMaxSimulationTime() {
        return valueOfMaxSimulationTime;
    }

    public void setValueOfMaxSimulationTime(int valueOfMaxSimulationTime) {
        this.valueOfMaxSimulationTime = valueOfMaxSimulationTime;
    }

    public int getValueOfMinArrivalTime() {
        return valueOfMinArrivalTime;
    }

    public void setValueOfMinArrivalTime(int valueOfMinArrivalTime) {
        this.valueOfMinArrivalTime = valueOfMinArrivalTime;
    }

    public int getValueOfMaxArrivalTime() {
        return valueOfMaxArrivalTime;
    }

    public void setValueOfMaxArrivalTime(int valueOfMaxArrivalTime) {
        this.valueOfMaxArrivalTime = valueOfMaxArrivalTime;
    }

    public int getValueOfMinServiceTime() {
        return valueOfMinServiceTime;
    }

    public void setValueOfMinServiceTime(int valueOfMinServiceTime) {
        this.valueOfMinServiceTime = valueOfMinServiceTime;
    }

    public int getValueOfMaxServiceTime() {
        return valueOfMaxServiceTime;
    }

    public void setValueOfMaxServiceTime(int valueOfMaxServiceTime) {
        this.valueOfMaxServiceTime = valueOfMaxServiceTime;
    }

    public boolean getNumberOfClients() {
        if (N != null && N.getText() != null) {
            setValueOfN(Integer.parseInt(N.getText()));
            valueOfN = Integer.parseInt(N.getText());
            return true;
        }
        return false;
    }

    public boolean getNumberOfQueues() {
        if (Q != null && Q.getText() != null) {
            setValueOfQ(Integer.parseInt(Q.getText()));
            valueOfQ = Integer.parseInt(Q.getText());
            return true;
        }
        return false;
    }

    public boolean getMaxSimulationTime() {
        if (maxSimulationTime != null && maxSimulationTime.getText() != null) {
            setValueOfMaxSimulationTime(Integer.parseInt(maxSimulationTime.getText()));
            valueOfMaxSimulationTime = Integer.parseInt(maxSimulationTime.getText());
            return true;
        }
        return false;
    }

    public boolean getMaxArrivalTime() {
        if (maxArrivalTime != null && maxArrivalTime.getText() != null) {
            setValueOfMaxArrivalTime(Integer.parseInt(maxArrivalTime.getText()));
            valueOfMaxArrivalTime = Integer.parseInt(maxArrivalTime.getText());
            return true;
        }
        return false;
    }

    public boolean getMaxServiceTime() {
        if (maxServiceTime != null && maxServiceTime.getText() != null) {
            setValueOfMaxServiceTime(Integer.parseInt(maxServiceTime.getText()));
            valueOfMaxServiceTime = Integer.parseInt(maxServiceTime.getText());
            return true;
        }
        return false;
    }

    public boolean getMinServiceTime() {
        if (minServiceTime != null && minServiceTime.getText() != null) {
            setValueOfMinServiceTime(Integer.parseInt(minServiceTime.getText()));
            valueOfMinServiceTime = Integer.parseInt(minServiceTime.getText());
            return true;
        }
        return false;
    }

    public boolean getMinArrivalTime() {
        if (minArrivalTime != null && minArrivalTime.getText() != null) {
            setValueOfMinArrivalTime(Integer.parseInt(minArrivalTime.getText()));
            valueOfMinArrivalTime = Integer.parseInt(minArrivalTime.getText());
            return true;
        }
        return false;
    }

    public boolean inputsValidated() {
        if (getNumberOfClients() && getNumberOfQueues() && getMaxSimulationTime() && getMinArrivalTime() && getMaxArrivalTime() && getMinServiceTime() && getMaxServiceTime()) {
            return true;
        }
        return false;
    }

    public void start() throws InterruptedException {
        if (inputsValidated()) {
            SimulationManager simulationManager = new SimulationManager(valueOfN, valueOfQ, valueOfMaxSimulationTime, valueOfMinArrivalTime, valueOfMaxArrivalTime, valueOfMinServiceTime, valueOfMaxServiceTime);
            simulationManager.t.start();
            Thread.sleep(500);
        }
        BufferedReader buff = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("queue.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
           if(displayTextArea != null){
               displayTextArea.clear();
               displayTextArea.setText("");
           }
            buff = new BufferedReader(fileReader);
            String str;
            while ((str = buff.readLine()) != null) {
                displayTextArea.appendText("\n" + str);
            }
        } catch (IOException e) {
        } finally {
            try {
                fileReader.close();
            } catch (Exception ex) {
            }
        }
    }


}

package app.myapplication;

import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.fuel.FuelLevelCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;
import com.github.pires.obd.commands.temperature.TemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.util.Timer;

//***********  Marlon's Tab ************************//

public class TabFragment3 extends Fragment {
    public TextView textRPM = null;
    public TextView textSpeed = null;
    public TextView textRPMFrequency = null;
    public TextView textSpeedFrequency = null;
    public LineGraphSeries<DataPoint> rpmSeries = null;
    public LineGraphSeries<DataPoint> speedSeries = null;
    public View view = null;
    public CountDownTimer counter = null;
    public GraphView graphRPM = null;
    public GraphView graphSpeed = null;
    public TextView textEngineCoolantTemp = null;
    public RPMCommand engineRpmCommand = null;
    public SpeedCommand speedCommand = null;
    public EngineCoolantTemperatureCommand engineCoolantTemperatureCommand = null;
    public BluetoothSocket socket = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_3, container, false);

        try {
            //************* -- FOR TESTING COMMENT OUT THIS BLOCK CODE -- START HERE -- ************************
           // /*
            BluetoothSocket socket = ((MyApplication) getActivity().getApplication()).getGlobalBluetoothSocket();
            new EchoOffCommand().run(socket.getInputStream(), socket.getOutputStream());
            new LineFeedOffCommand().run(socket.getInputStream(), socket.getOutputStream());
            new TimeoutCommand(125).run(socket.getInputStream(), socket.getOutputStream());
            new SelectProtocolCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream());
            // new AmbientAirTemperatureCommand().run(socket.getInputStream(), socket.getOutputStream());

            engineRpmCommand = new RPMCommand();
            speedCommand = new SpeedCommand();
            engineCoolantTemperatureCommand = new EngineCoolantTemperatureCommand();
            //final FuelLevelCommand fuelLevelCommand = new FuelLevelCommand();
            //*/
            //************** -- END HERE -- END HERE -- END HERE -- ********************************************

            rpmSeries = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0, 0)});

            speedSeries = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0, 0)});

            graphRPM = (GraphView) view.findViewById(R.id.graphRPM);
            graphSpeed = (GraphView) view.findViewById(R.id.graphSpeed);

            Viewport graphViewRPM = graphRPM.getViewport();
            Viewport graphViewSpeed = graphSpeed.getViewport();

            graphRPM.setBackgroundColor(Color.rgb(55, 55, 55));
            graphRPM.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
            graphRPM.getGridLabelRenderer().setGridColor(Color.rgb(204, 229, 255));
            graphRPM.getGridLabelRenderer().setHorizontalLabelsColor(Color.rgb(204, 229, 255));
            graphRPM.getGridLabelRenderer().setVerticalLabelsColor(Color.rgb(204, 229, 255));
            graphRPM.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.rgb(204, 229, 255));
            graphRPM.getGridLabelRenderer().reloadStyles();
            rpmSeries.setColor(Color.rgb(204, 0, 0));

            graphSpeed.setBackgroundColor(Color.rgb(55, 55, 55));
            graphSpeed.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
            graphSpeed.getGridLabelRenderer().setGridColor(Color.rgb(204, 229, 255));
            graphSpeed.getGridLabelRenderer().setHorizontalLabelsColor(Color.rgb(204, 229, 255));
            graphSpeed.getGridLabelRenderer().setVerticalLabelsColor(Color.rgb(204, 229, 255));
            graphSpeed.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.rgb(204, 229, 255));
            graphSpeed.getGridLabelRenderer().reloadStyles();
            speedSeries.setColor(Color.rgb(204, 0, 0));

            //set graphs to be scrollable
            graphViewRPM.setScrollable(true);
            graphViewSpeed.setScrollable(true);

            //subject to change
            //for now RPM bounds will be x-axis: 0 to 100 y-axis: 0 - 3000
            graphViewRPM.setXAxisBoundsManual(true);
            graphViewRPM.setMinX(0);
            graphViewRPM.setMaxX(100);

            graphViewRPM.setYAxisBoundsManual(true);
            graphViewRPM.setMinY(0);
            graphViewRPM.setMaxY(3000);

            //subject to change
            //for now Speed bounds will be x-axis: 0 to 100 y-axis: 0 - 100
            graphViewSpeed.setXAxisBoundsManual(true);
            graphViewSpeed.setMinX(0);
            graphViewSpeed.setMaxX(100);

            graphViewSpeed.setYAxisBoundsManual(true);
            graphViewSpeed.setMinY(0);
            graphViewSpeed.setMaxY(100);

            graphRPM.addSeries(rpmSeries);
            graphSpeed.addSeries(speedSeries);

            textRPM = (TextView) view.findViewById(R.id.textRPM);
            textSpeed = (TextView) view.findViewById(R.id.textSpeed);
            textRPMFrequency = (TextView) view.findViewById(R.id.textRPMFrequency);
            textSpeedFrequency = (TextView) view.findViewById(R.id.textSpeedFrequency);
            textEngineCoolantTemp = (TextView) view.findViewById(R.id.textEngineCoolantTemp);
            //final TextView textFuelLevel = (TextView) view.findViewById(R.id.textFuelLevel);
            //final ProgressBar progressBarFuelLevel = (ProgressBar) view.findViewById(R.id.fuelProgressBar);
            //progressBarFuelLevel.setMax(100);

            setUserVisibleHint(isVisible());

        } catch (Exception e) {
            TextView rpmT2 = (TextView) view.findViewById(R.id.textRPM);
            rpmT2.setText("We messed up in the while loop");

        }

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && textEngineCoolantTemp != null) {
            rpmSeries.resetData(new DataPoint[]{
                    new DataPoint(0, 0)});

            speedSeries.resetData(new DataPoint[]{
                    new DataPoint(0, 0)});
            counter = new CountDownTimer(18000000, 500) {
                int i = 0;
                public void onTick(long millisUntilFinished) {
                    if (!Thread.currentThread().isInterrupted()) {
                        try {
                            //************* -- FOR TESTING COMMENT OUT THIS BLOCK CODE -- START HERE -- ************************
                            ///*
                            engineRpmCommand.run(socket.getInputStream(), socket.getOutputStream());
                            speedCommand.run(socket.getInputStream(), socket.getOutputStream());
                            engineCoolantTemperatureCommand.run(socket.getInputStream(),socket.getOutputStream());
                           // fuelLevelCommand.run(socket.getInputStream(),socket.getOutputStream());

                            // TODO handle commands result
                            String rpmString = engineRpmCommand.getFormattedResult();
                            String speedString = speedCommand.getFormattedResult();
                            String engineCoolantTempString = engineCoolantTemperatureCommand.getFormattedResult();
                            //String fuelLevelString = fuelLevelCommand.getFormattedResult();

                            textRPM.setText("RPM: " + rpmString);
                            textSpeed.setText("Speed: " + speedString);
                            textEngineCoolantTemp.setText("Engine Coolant Temp: " + engineCoolantTempString);
                            //textFuelLevel.setText("Fuel Level: " + fuelLevelString);

                            int rpmNum = engineRpmCommand.getRPM();
                            int speedNum = speedCommand.getMetricSpeed();
                            i++;

                            rpmSeries.appendData(new DataPoint(i,rpmNum),true,100);
                            speedSeries.appendData(new DataPoint(i,speedNum),true,100);
                            //int fuelProgress = (int) (fuelLevelCommand.getFuelLevel() * 100);
                            //progressBarFuelLevel.setProgress(fuelProgress);



                            float rpmFrequency = (float)rpmNum/60;
                            float speedFrequency = (float)speedNum/60;
                            textRPMFrequency.setText("Frequency: " + String.format("%.3f",rpmFrequency) + "Hz");
                            textSpeedFrequency.setText("Frequency: " + String.format("%.3f",speedFrequency) + "Hz");
                           // */
                            //************** -- END HERE -- END HERE -- END HERE -- ********************************************

                            //*****--- UNCOMMENT THE FOLLOWING BLOCK OF CODE IN ORDER TO TEST WITHOUT ----*********
                            //*****--- CONNECTING TO OBDII ----***************************************************
                            /*
                            i++;
                            int rpmNum = i;
                            int speedNum = i;
                            textRPM.setText("RPM: " + rpmNum);
                            textSpeed.setText("Speed: " + speedNum);
                            rpmSeries.appendData(new DataPoint(i,rpmNum),true,100);
                            speedSeries.appendData(new DataPoint(i,speedNum),true,100);
                            float rpmFrequency = (float)rpmNum/60;
                            float speedFrequency = (float)speedNum/60;
                            textRPMFrequency.setText("Frequency: " + String.format("%.3f",rpmFrequency) + "Hz");
                            textSpeedFrequency.setText("Frequency: " + String.format("%.3f",speedFrequency) + "Hz");
                            */

                        } catch (Exception e) {
                            //
                            textRPM.setText("Ex: " + e);
                        }


                        //Log.d(TAG, "RPM: " + engineRpmCommand.getFormattedResult());
                        // Log.d(TAG, "Speed: " + speedCommand.getFormattedResult());

                        //Thread.sleep(500);
                    }
                }
                public void onFinish() {
                    TextView timerT = (TextView) view.findViewById(R.id.textRPM);
                    timerT.setText("Time!");

                }
            };
            counter.start();
        } else {
            if (counter != null) {
                counter.cancel();
                counter = null;
            }
        }
    }
}
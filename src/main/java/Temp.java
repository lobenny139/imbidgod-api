import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.regression.*;

public class Temp {
    public static void main(String[] args){
        SimpleRegression regression = new SimpleRegression();
        regression.addData(new double[][]{
                {0,0},
                {-0.2,-0.358669331},
                        {-0.4,-0.629418342},
                        {-0.6,-0.804642473},
                        {-0.8,-0.877356091},
                        {-1,-0.841470985},
                        {-1.2,-0.692039086},
                        {-1.4,-0.42544973},
                        {-1.6,-0.039573603},
                        {-1.618,0}

                //{400,250001.5338}
        }
        );
        double slope = regression.getSlope();
        double intercept = regression.getIntercept();
        double predict = regression.predict(-2.8);
        System.out.println(slope);
        System.out.println(intercept);
        System.out.println(predict);
    }
}

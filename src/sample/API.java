package sample;
import java.util.*;
import java.net.*;
import io.indico.Indico;
import io.indico.api.utils.IndicoException;
import io.indico.api.results.IndicoResult;
import java.util.Map;
import java.util.ArrayList;


public class API {

    ArrayList<Pair<String,Double>> recognise(String imgPath) throws Exception {
        ArrayList<Pair<String,Double>> output = new ArrayList<Pair<String,Double>>();
        double max;
        String maxKey="";
        String filename;
        try {
            Indico indico = new Indico("1558e05956ba46730408f2b2d5b34480");
            IndicoResult single = indico.imageRecognition.predict(imgPath);
            Map<String, Double> result = single.getImageRecognition();
            for (int i = 0; i < 4; i++) {
                max = Collections.max(result.values());
                filename = "";
                for (HashMap.Entry<String, Double> entry : result.entrySet()) {
                    String key = entry.getKey();
                    Double value = entry.getValue();
                    if (max == value) {
                        maxKey = key;
                        String [] array = key.split(",",2);
                        filename = array[0];
                        Pair<String, Double> pair = new Pair<String, Double>();
                        pair.first = filename;
                        pair.second = max;
                        output.add(pair);
                    }
                }
                result.remove(maxKey);
            }
        }catch(IndicoException a){
            throw new Exception("Invalid API key");
        }catch(UnknownHostException x){
            throw new Exception("Internet connection lost");
        } catch (Exception e){
            throw e;
        }
        return output;
    }
}


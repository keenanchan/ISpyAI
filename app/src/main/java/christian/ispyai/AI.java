package christian.ispyai;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Tag;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import christian.ispyai.Fragments.Camera.MainFragment;

/**
 * Created by tgkokk on 27/01/2018.
 */

public class AI {
    private final static VisionServiceClient visionServiceClient = new VisionServiceRestClient("a19a1981aaee438284054ae76e32d27b", "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");

    public static class AsyncClass extends AsyncTask<Void, Void, List<Tag>> {
        private final static String TAG = "AsyncClass";

        private Exception e;

        private ByteArrayInputStream inputStream;
        private MainFragment.OnGuessListener callback;

        public AsyncClass(ByteArrayInputStream inputStream, MainFragment.OnGuessListener callback) {
            super();
            this.inputStream = inputStream;
            this.callback = callback;
        }

        @Override
        protected List<Tag> doInBackground(Void... voids) {
            String[] features = {"Tags"};
            String[] details = {};

            try {
                AnalysisResult result = visionServiceClient.analyzeImage(inputStream, features, details);
                return result.tags;
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(List<Tag> tags) {
            callback.onGuess(tags);
        }
    }
}

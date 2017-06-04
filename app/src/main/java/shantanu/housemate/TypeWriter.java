package shantanu.housemate;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.Delayed;

/**
 * Created by SHAAN on 07-04-17.
 */
public class TypeWriter extends TextView {

    private CharSequence[] mText;
    private int noOfMsgs = 0, i;
    private int mIndex;
    private long mDelay = 150;

    boolean isWriting = true;
    boolean isDeleting = false;

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHandler = new Handler();

    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            if(isWriting) {
                //Log.i("TYPEWRITER", "is writing: " + mText[i].subSequence(0, mIndex));
                setText(mText[i].subSequence(0, mIndex++));
                if (mIndex <= mText[i].length()) {
                    mHandler.postDelayed(characterAdder, mDelay);
                } else {
                    setText(mText[i].subSequence(0, --mIndex));
                    isWriting = false;
                    isDeleting = true;
                    mIndex--;
                    mHandler.postDelayed(characterAdder, mDelay);
                    setCharacterDelay(100);
                }
            } else if (isDeleting) {
                //Log.i("TYPEWRITER", "is deleting: " + mText[i].charAt(mIndex));
                setText(mText[i].subSequence(0, mIndex--));
                switch (i) {
                    case 1:
                        setCharacterDelay(70);
                        break;
                    case 2:
                        setCharacterDelay(90);
                        break;

                }
                if (mIndex > 0) {
                    mHandler.postDelayed(characterAdder, mDelay);
                } else {
                    mIndex = 0;
                    setText("");
                    mHandler.removeCallbacks(characterAdder);
                    if ((i + 1) < noOfMsgs) {
                        i++;
                        switch (i) {
                            case 1:
                                setCharacterDelay(70);
                                break;
                            case 2:
                                setCharacterDelay(90);
                                break;

                        }
                        isWriting = true;
                        isDeleting = false;
                    } else {
                        i = 0;
                        setCharacterDelay(200);
                        isWriting = true;
                        isDeleting = false;
                    }
                    mHandler.postDelayed(characterAdder, mDelay);
                }
            }
        }
    };

    public void writeText(CharSequence[] txt) {
        mText = txt;
        mIndex = 0;
        noOfMsgs = txt.length;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long s) {
        mDelay = s;
    }

}

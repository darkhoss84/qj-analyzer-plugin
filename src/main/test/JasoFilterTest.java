
import com.queryjet.jaso.JasoDecomposer;
import com.queryjet.simplejaso.SimpleJasoDecomposer;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Created by nobaksan on 2015. 11. 19..
 */
public class JasoFilterTest {

//    public static void main(String[] args) throws IOException {
//        JasoAnalyzerTest kt = new JasoAnalyzerTest();
//
//        String source = "dkdlvhs";
//
//        long start = System.currentTimeMillis();
//
//        System.out.println("Korean analysis init");
//        JasoAnalyzer analyzer = new JasoAnalyzer();
//
//        TokenStream stream = analyzer.tokenStream("", new StringReader(source));
//
//        int loop_cnt = 0;
//        int loop_cnt2 = 0;
//        int end_offset = 0;
//        int term_offset = 0;
//
//        Map result_list = new HashMap();
//        ArrayList term_morph_list = new ArrayList();
//
//        String origin_text = "";
//
//        OffsetAttribute offSetAttr = null;
//        CharTermAttribute termAttr = null;
//        PositionIncrementAttribute posAttr = null;
//        stream.reset();
//        int idx = 0;
//        while (stream.incrementToken()) {
//            System.out.println("idx:" + idx);
//            idx++;
//            offSetAttr = (OffsetAttribute) stream.getAttribute(OffsetAttribute.class);
//            termAttr = (CharTermAttribute) stream.getAttribute(CharTermAttribute.class);
//            posAttr = (PositionIncrementAttribute) stream.getAttribute(PositionIncrementAttribute.class);
//
//            if (end_offset == 0) {
//                end_offset = offSetAttr.endOffset();
//                term_morph_list = new ArrayList();
//
//                term_offset++;
//            }
//
//            System.out.println(termAttr.toString());
//
//            System.out.println("-------------------------------");
//
//
//            System.out.println(System.currentTimeMillis() - start + "ms");
//        }
//        stream.end();
//        stream.close();
//    }

    public static void main(String[] args) throws Exception {
        System.out.println("START");
        SimpleJasoDecomposer jaso = new SimpleJasoDecomposer();
        //System.out.println(jaso.getTokenKor("프랑스  국가","edge"));
        System.out.println("END");
    }

}
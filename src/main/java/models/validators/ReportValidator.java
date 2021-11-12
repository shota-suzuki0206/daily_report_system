package models.validators;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import actions.views.ReportView;
import constants.MessageConst;

/**
 * 日報インスタンスに設定されている値のバリデーションを行うクラス
 */
public class ReportValidator {

    /**
     * 日報インスタンスの各項目についてバリデーションを行う
     * @param rv 日報インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(ReportView rv){
        List<String> errors = new ArrayList<String>();

        //タイトルのチェック
        String titleError = validateTitle(rv.getTitle());
        if(!titleError.equals("")) {
            errors.add(titleError);
        }

        //内容のチェック
        String contentError = validateContent(rv.getContent());
        if (!contentError.equals("")) {
            errors.add(contentError);
        }

        //出勤時間と退勤時間のチェック
        String startEndError = validateStartEndTime(rv.getStartTime(),rv.getEndTime());
        if (!startEndError.equals("")) {
            errors.add(startEndError);
        }

        /**
        //出勤時間のチェック
        String startError = validateStartTime(rv.getStartTime());
        if (!startError.equals("")) {
            errors.add(startError);
        }

        //退勤時間のチェック
        String endError = validateEndTime(rv.getEndTime());
        if (!endError.equals("")) {
            errors.add(endError);
        }
        */


        return errors;
    }

    /**
     * タイトルに入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param title タイトル
     * @return エラーメッセージ
     */
    private static String validateTitle(String title) {
        if(title == null || title.equals("")) {
            return MessageConst.E_NOTITLE.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param content 内容
     * @return エラーメッセージ
     */
    private static String validateContent(String content) {
        if(content == null || content.equals("")) {
            return MessageConst.E_NOCONTENT.getMessage();

        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 出勤時間と退勤時間、両方のの入力があった場合
     * 退勤時間の入力値が、出勤時間より早い時間に設定されていないか
     * @param startTime 出勤時間
     * @param endTime 退勤時間
     * @return エラーメッセージ
     */
    private static String validateStartEndTime(String startTime,String endTime) {
        if(startTime != null && !startTime.equals("") ) {
            if(endTime != null && !endTime.equals("")) {

                LocalTime s1 = LocalTime.parse(startTime);
                LocalTime e1 = LocalTime.parse(endTime);
                boolean se = s1.isAfter(e1);
                if(se == true) {
                    return MessageConst.E_NOSTARTENDTIME.getMessage();
                }
            }
        }
        return "";
    }



    /**
     * 出勤時間に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param startTime 出勤時間
     * @return エラーメッセージ
     */
    /*
    private static String validateStartTime(String startTime) {
        if(startTime == null || startTime.equals("")) {
            return MessageConst.E_NOSTARTTIME.getMessage();

        }

        //入力値がある場合は空文字を返却
        return "";
    }
    */
    /**
     * 退勤時間に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param endTime 退勤時間
     * @return エラーメッセージ
     */
    /*
    private static String validateEndTime(String endTime) {
        if(endTime == null || endTime.equals("")) {
            return MessageConst.E_NOENDTIME.getMessage();

        }

        //入力値がある場合は空文字を返却
        return "";
    }
    */

}

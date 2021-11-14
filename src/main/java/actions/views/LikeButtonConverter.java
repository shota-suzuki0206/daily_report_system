package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.LikeButton;

/**
 * いいねデータのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class LikeButtonConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param rv LikeButtonViewのインスタンス
     * @return LikeButtonのインスタンス
     */
    public static LikeButton toModel(LikeButtonView lbv) {
        return new LikeButton(
                lbv.getId(),
                EmployeeConverter.toModel(lbv.getEmployee()),
                ReportConverter.toModel(lbv.getReport()),
                lbv.getCreatedAt(),
                lbv.getUpdatedAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param lb LikeButtonのインスタンス
     * @return LikeButtonViewのインスタンス
     */
    public static LikeButtonView toView(LikeButton lb) {
        if(lb == null) {
            return null;
        }

        return new LikeButtonView(
                lb.getId(),
                EmployeeConverter.toView(lb.getEmployee()),
                ReportConverter.toView(lb.getReport()),
                lb.getCreatedAt(),
                lb.getUpdatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<LikeButtonView> toViewList(List<LikeButton> list){
        List<LikeButtonView> evs = new ArrayList<>();


        for (LikeButton lb : list) {
            evs.add(toView(lb));
        }

        return evs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル(コピー先)
     * @param rv Viewモデル(コピー元)
     */
    public static void copyViewToModel(LikeButton lb, LikeButtonView lbv) {
        lb.setId(lbv.getId());
        lb.setEmployee(EmployeeConverter.toModel(lbv.getEmployee()));
        lb.setReport(ReportConverter.toModel(lbv.getReport()));
        lb.setCreatedAt(lbv.getCreatedAt());
        lb.setUpdatedAt(lbv.getUpdatedAt());

    }
}

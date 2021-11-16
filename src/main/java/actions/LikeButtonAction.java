package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.LikeButtonView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.LikeButtonService;
import services.ReportService;

/**
 * いいねに関する処理を行うActionクラス
 *
 */
public class LikeButtonAction extends ActionBase {

    private LikeButtonService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new LikeButtonService();

        //メソッドを実行
        invoke();
        service.close();
    }

    /**
     * いいねの一覧画面を表示する
     */
    public void index() throws ServletException, IOException {

        //セッションからログイン中の従業員情報を取得
        EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //ログイン中の従業員がいいねした日報データを、指定されたページ数の一覧画面に表示する分取得する
        int page = getPage();
        List<LikeButtonView> likeButtons = service.getMinePerPage(loginEmployee, page);

        //ログイン中の従業員がいいねした日報データの件数を取得
        long myLikeButtonsCount = service.countAllMine(loginEmployee);

        putRequestScope(AttributeConst.LIKEBUTTONS, likeButtons); //取得した日報データ
        putRequestScope(AttributeConst.LIK_COUNT, myLikeButtonsCount); //ログイン中の従業員が作成した日報の数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);//一ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_LIK_INDEX);
    }

    /**
     * いいねの登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策tokenのチェック
        if (checkToken()) {

            //パラメータから日報のidを取得
            Integer id = Integer.parseInt(request.getParameter("id"));

            // ReportServiceインスタンスを生成
            ReportService rs = new ReportService();

            // idを条件に日報データを取得する
            ReportView rv = rs.findOne(id);

            //パラメータの値をもとにLikeButtonのインスタンスを作成する
            LikeButtonView lbv = new LikeButtonView(
                    null,
                    getSessionScope(AttributeConst.LOGIN_EMP), //ログインしている従業員を、日報作成者として登録する
                    rv,
                    null,
                    null);

            //いいね情報登録
            service.create(lbv);

            putRequestScope(AttributeConst.FLUSH, MessageConst.I_PUSHLIKE.getMessage());
            putRequestScope(AttributeConst.REPORT, rv); //いいねされた日報情報

            //詳細画面を再表示
            forward(ForwardConst.FW_REP_SHOW);
        }
    }

    /**
     * いいね削除を行う
     * @throws ServletException
     * @throws IOException
     */

    public void destroy() throws ServletException, IOException {

        //CSRF対策tokenのチェック
        if (checkToken()) {

            //パラメータから日報のidを取得
            Integer id = Integer.parseInt(request.getParameter("id"));

            // ReportServiceインスタンスを生成
            ReportService rs = new ReportService();

            // idを条件に日報データを取得する
            ReportView rv = rs.findOne(id);

            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            service.destroy(rv,ev);

            //セッションに削除完了のフラッシュメッセージを設定
            putRequestScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());
            putRequestScope(AttributeConst.REPORT, rv); //いいねされた日報情報

            //詳細画面を再表示
            forward(ForwardConst.FW_REP_SHOW);
        }
    }
}

package demo.todo.jfinal;

import act.Act;
import act.job.OnAppStart;
import demo.todo.jfinal.model.Account;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.With;
import org.osgl.mvc.result.RenderJSON;
import org.piaohao.act.jfinal.db.JFinalTransactional;

import java.util.List;

import static act.controller.Controller.Util.render;
import static act.controller.Controller.Util.renderJson;

/**
 * A Simple Todo application controller
 */
public class Todo {

    @GetAction("/list")
    public void list() {
        List<Account> accounts = Account.dao.find("select * from account");
        render(accounts);
    }

    @GetAction("/save")
    @With(JFinalTransactional.class)
    public RenderJSON save() {
        new Account().set("name", "act test001").set("password", "123").save();
        return renderJson("yes");
    }

    public static void main(String[] args) throws Exception {
        Act.start("TODO-JFinal");
    }

}

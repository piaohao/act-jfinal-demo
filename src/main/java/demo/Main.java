package demo;

import act.Act;
import act.app.data.StringValueResolverManager;
import act.controller.annotation.UrlContext;
import act.inject.DefaultValue;
import demo.model.Account;
import org.osgl.$;
import org.osgl.mvc.annotation.*;
import org.piaohao.act.jfinal.db.JFinalTransactional;

import java.util.List;

import static act.controller.Controller.Util.notFoundIfNull;

/**
 * A Simple application demonstrate how to integrate JFinal template and db layer into ActFramework
 */
@UrlContext("/accounts")
public class Main {

    @GetAction
    public List<Account> list() {
        return Account.dao.find("select * from account");
    }

    @GetAction("{id}")
    public Account fetch(String id) {
        return Account.dao.findById(id);
    }

    @PostAction
    @With(JFinalTransactional.class)
    public Account create(String name, String password) {
        Account newAccount = new Account();
        newAccount.setName(name);
        newAccount.setPassword(password);
        newAccount.save();
        return newAccount;
    }

    @DeleteAction("{id}")
    @With(JFinalTransactional.class)
    public void delete(String id) {
        Account.dao.deleteById(id);
    }

    @PutAction("{id}/{field}")
    @With(JFinalTransactional.class)
    public void update(String id, String field, String value, @DefaultValue("String.class") String type, StringValueResolverManager resolver) {
        Object setValue = value;
        if (!"String.class".equals(type)) {
            if (!type.endsWith(".class")) {
                type = type + ".class";
            }
            setValue = resolver.resolve(value, $.classForName(type, Act.app().classLoader()));
        }
        Account account = Account.dao.findById(id);
        notFoundIfNull(account);
        account.set(field, setValue).save();
    }

    public static void main(String[] args) throws Exception {
        Act.start("ACT-JFinal-Demo");
    }

}

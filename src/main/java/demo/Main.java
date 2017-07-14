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

    /**
     * Fetch a list of accounts
     *
     * HTTP request sample:
     *
     * ```
     * GET /accounts      # in normal html response, template generate response
     * GET /accounts/json # in JSON response, framework generate response
     * ```
     */
    @GetAction
    public List<Account> list() {
        return Account.dao.find("select * from account");
    }


    /**
     * Fetch an account by id
     *
     * HTTP request sample:
     *
     * ```
     * GET /accounts/123         # request an account with ID = 123
     * GET /accounts?id=123      # request an account with ID = 123
     * GET /accounts/json?id=123 # request an account with ID = 123 in JSON response
     * GET /accounts/123/json    # request an account with ID = 123 in JSON response
     * ```
     */
    @GetAction("{id}")
    public Account fetch(String id) {
        return Account.dao.findById(id);
    }

    /**
     * Create an new account
     *
     * HTTP request sample
     *
     * ```
     * POST /accounts
     * ```
     *
     * The post body should be different when `Content-Type` is different:
     *
     * # Content-Type = application/x-www-form-urlencoded
     * ```
     * account.name=张三
     * account.password=li-si
     * ```
     *
     * # Content-Type = application/json

     * ```
     * {
     *     "name": "张三"
     *     "password: "li-si"
     * }
     * ```
     *
     * or
     *
     * ```
     * {
     *      "account" : {"name": "张三", "password: "li-si"}
     * }
     * ```
     *
     */
    @PostAction
    @With(JFinalTransactional.class)
    public Account create(Account account) {
        account.save();
        return account;
    }

    /**
     * Delete an account by ID
     *
     * HTTP Request sample
     *
     * ```
     * DELETE /accounts/123
     * DELETE /accounts?id=123
     * ```
     */
    @DeleteAction("{id}")
    @With(JFinalTransactional.class)
    public void delete(String id) {
        Account.dao.deleteById(id);
    }

    /**
     * Update an account field value
     *
     * HTTP Request sample
     *
     * ```
     * PUT /accounts/123/name?value=李四
     * PUT /accounts/123/password?value=zhang-san
     * PUT /accounts/123/age?value=18&type=int
     * ```
     *
     * # passing all params in query parameter is also okay, easy for front end to serialize entire js object:
     *
     * ```
     * PUT /accounts?id=123&field=age&value=18&type=int
     * ```
     */
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

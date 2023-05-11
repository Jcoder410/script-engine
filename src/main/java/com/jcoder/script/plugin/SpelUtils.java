package com.jcoder.script.plugin;

import org.graalvm.polyglot.TypeLiteral;
import org.graalvm.polyglot.Value;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * spel表达式计算
 *
 * @author Jcoder
 */
public class SpelUtils {

    public Object execute(String expressionStr, Value param) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(param.as(new TypeLiteral<Map<String, Object>>() {
        }));
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(expressionStr);
        Object result = expression.getValue(context, Object.class);
        return result;
    }
}

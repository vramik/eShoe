grammar SearchQueryLanguage;

options {
    language = Java;
    output = AST;
}

@lexer::header {
  package com.issuetracker.search.ql;
}

@parser::header {
  package com.issuetracker.search.ql;
}

//lexer rules
WS : ( ' ' | '\t' | '\r' | '\n' )+ { $channel = HIDDEN; };

FIELD_NAME: ('a'..'z' | '0'..'9' | '_')+;
FIELD_VALUE: DOUBLE_QUOTE ~('"')* DOUBLE_QUOTE;
DOUBLE_QUOTE: '"';
AND: 'AND';
IN: 'IN';
GT: '>';
LT: '<';
GTE: '>=';
LTE: '<=';

//parser rules
query: andExpression;
andExpression: expression (AND! expression)*;
expression: equals | in | tilda | lt | gt | lte | gte;
equals: FIELD_NAME '='^ fieldValue;
in: FIELD_NAME IN^ '('! fieldValue (','! fieldValue)* ')'!;
tilda: FIELD_NAME '~'^ fieldValue;
lt: FIELD_NAME LT^ fieldValue;
gt: FIELD_NAME GT^ fieldValue;
gte: FIELD_NAME GTE^ fieldValue;
lte: FIELD_NAME LTE^ fieldValue;
fieldValue: FIELD_VALUE;



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

//parser rules
query: andExpression;
andExpression: expression (AND! expression)*;
expression: equals | in | tilda;
equals: FIELD_NAME '='^ fieldValue;
in: FIELD_NAME IN^ '('! fieldValue (','! fieldValue)* ')'!;
tilda: FIELD_NAME '~'^ fieldValue;
fieldValue: FIELD_VALUE;



% Suma de dos numeros
suma(A, B, R) :-
    number(A), number(B), R is A + B.
% Resta de dos numeros
resta(A, B, R) :-
    number(A), number(B), R is A - B.
% Multiplicacion de dos numeros
multiplicacion(A, B, R) :-
    number(A), number(B), R is A * B.
% Division de dos numeros (falla si B es 0); el 1.0 fuerza resultado decimal
division(A, B, R) :-
    number(A), number(B), B =\= 0, R is A * 1.0 / B.

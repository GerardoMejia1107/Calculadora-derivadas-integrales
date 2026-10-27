% Suma de dos numeros
suma(A, B, R) :-
    number(A), number(B), R is A + B.
% Resta de dos numeros
resta(A, B, R) :-
    number(A), number(B), R is A - B.

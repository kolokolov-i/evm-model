mov R0, 0
mov R3, 1
mov R1, 0
mov R2, 6
put P4, R0
put P5, R1
put P6, R2
put P7, R3
int 9
mov R1, 8
mov R2, 0
put P5, R1
put P6, R2
int 9
mov R1, 0
mov R2, 0xFA
put P5, R1
put P6, R2
int 9
mov R1, 0xF8
mov R2, 0
put P5, R1
put P6, R2
int 9
mov R0, 1
mov R3, 12
mov R1, 0
mov R2, 6
put P4, R0
put P5, R1
put P6, R2
put P7, R3
int 9
mov R0, 0
mov R1, 4
mov R2, 4
put P4, R0
put P5, R1
put P6, R2
int 9
mov R1, 4
mov R2, 0xFC
put P5, R1
put P6, R2
int 9
mov R0, 1
mov R3, 14
mov R1, 2
mov R2, 2
put P4, R0
put P5, R1
put P6, R2
put P7, R3
int 9
mov R0, 0
mov R1, 0
mov R2, 2
put P4, R0
put P5, R1
put P6, R2
int 9
mov R1, 4
mov R2, 0
put P5, R1
put P6, R2
int 9
mov R1, 0
mov R2, 0xFE
put P5, R1
put P6, R2
int 9
mov R1, 0xFC
mov R2, 0
put P5, R1
put P6, R2
int 9
mov R0, 1
mov R3, 2
mov R1, 1
mov R2, 7
put P4, R0
put P5, R1
put P6, R2
put P7, R3
int 9
mov R0, 0
mov R1, 0
mov R2, 3
put P4, R0
put P5, R1
put P6, R2
int 9
mov R1, 1
mov R2, 0
put P5, R1
put P6, R2
int 9
mov R1, 0
mov R2, 0xFE
put P5, R1
put P6, R2
int 9
mov R0, 0xFF
put P0, R0
int 8
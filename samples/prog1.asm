mov R0, 0
mov R1, 0
mov R2, 0
mov R3, 0
mov R4, 0
mov R5, 2
mov R6, 0
mov R7, 10
Loop:
storx [RM1], RM0
inc RM0
add RM1, RM2
cmp RM0, RM3
jmpnz Loop
mov R0, 1
int 7
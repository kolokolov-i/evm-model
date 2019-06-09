mov RM0, 0
mov RM1, 0
mov RM2, 2
mov RM3, 10
Loop:
storx [RM1], RM0
inc RM0
add RM1, RM2
cmp RM0, RM3
jmpnz Loop
mov R0, 0xFF
put P0, R0
int 8
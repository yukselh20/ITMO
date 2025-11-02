# OPD Lab 6: Interrupt-Driven I/O and Concurrency Control

This repository contains the solution and analysis for the sixth laboratory assignment of the "Fundamentals of Professional Activities" course. The project involves developing and analyzing a complex, interrupt-driven program for a basic computer architecture. The program consists of a main background task that continuously modifies a shared variable, and two independent Interrupt Service Routines (ISRs) that are triggered by external devices to perform calculations and update the shared state. Key concepts include interrupt handling, concurrency control using atomic operations, and data integrity enforcement through domain constraints.

| **Course**            | Fundamentals of Professional Activities (OPD) |
| --------------------- | --------------------------------------------- |
| **Lab No.**           | 6                                             |
| **Variant**           | 2462                                          |
| **Student**           | Hamza Yüksel                                  |
| **Group**             | P3132                                         |
| **Instructor**        | I. V. Perminov (based on similar report)      |

## Table of Contents
- [Assignment Description](#assignment-description)
- [Program Purpose and Functionality](#program-purpose-and-functionality)
- [System Architecture](#system-architecture)
  - [Interrupt-Driven Model](#interrupt-driven-model)
  - [The Main Loop (Background Task)](#the-main-loop-background-task)
  - [Interrupt Service Routines (ISRs)](#interrupt-service-routines-isrs)
  - [Concurrency Control](#concurrency-control)
  - [ODZ Enforcement Subroutine](#odz-enforcement-subroutine)
- [Data Representation and Valid Domain (ODZ)](#data-representation-and-valid-domain-odz)
- [Memory Map](#memory-map)
- [Assembler Source Code](#assembler-source-code)
- [Conclusion](#conclusion)

## Assignment Description

Based on the given variant (2462), the task is to develop and analyze an interrupt-driven program complex. The main program must modify the contents of a specific memory location (`X`), while interrupt handlers perform data exchange with external devices.

1.  **Main Program:** Continuously decrement the value of `X` (at address `0x033`) by 2 in a loop.
2.  **Interrupt Handler for VU-1:** On a ready signal from VU-1, calculate the function `F(X) = 4X - 8` and output the 8-bit signed result to the device.
3.  **Interrupt Handler for VU-3:** On a ready signal from VU-3, read an 8-bit signed value from its data register, subtract the current value of `X` from it, and write the result back into `X`.
4.  **Valid Domain (ODZ) Control:** If at any point the value of `X` goes outside its defined valid domain, it must be reset to the maximum value of the domain.
5.  All other interrupts from unused devices must be ignored.

## Program Purpose and Functionality

The program implements a concurrent system with three primary components:
1.  **Main Program:** Acts as a background task that cyclically and atomically decrements a shared 16-bit signed variable `X`. It also ensures `X` remains within its valid domain (ODZ).
2.  **VU-1 Interrupt Handler:** An event-driven task triggered by VU-1. It calculates the function `F(X) = 4X - 8` based on the *current* value of `X` and outputs the 8-bit result to the device. This operation is non-destructive to `X`.
3.  **VU-3 Interrupt Handler:** An event-driven task triggered by VU-3. It reads an 8-bit signed value from the device, sign-extends it to 16 bits, calculates a new value `NewX = (DeviceValue) - X`, and destructively updates the shared variable `X` with this new value, ensuring the result adheres to the ODZ.

## System Architecture

### Interrupt-Driven Model
Unlike polling, this program uses an interrupt-driven model. The main loop runs independently until an external device (VU-1 or VU-3) sends an interrupt signal. The CPU then suspends the main loop, saves its state, and jumps to the corresponding Interrupt Service Routine (ISR) specified in the Interrupt Vector Table (IVT).

### The Main Loop (Background Task)
The main loop at `MAIN_LOOP` continuously performs a critical operation: `X = X - 2`. To prevent race conditions, this operation is made **atomic**.

### Interrupt Service Routines (ISRs)
-   **`INT1_HANDLER` (for VU-1):** A read-only handler regarding `X`. It saves the accumulator, loads `X`, computes `4*X - 8` (using two `ASL` instructions), outputs the result to VU-1's data register (port `0x2`), and restores the accumulator before returning.
-   **`INT3_HANDLER` (for VU-3):** A read-write handler. It reads an 8-bit signed value from VU-3 (port `0x6`), uses the `SXTB` (Sign-Extend Byte) instruction to convert it to a 16-bit signed integer, subtracts the current `X`, and then updates `X` with the new value.

### Concurrency Control
The main loop's modification of `X` is susceptible to race conditions. For example, an interrupt could occur after `LD X` but before `ST X`, causing the ISR to work with a stale value or have its own update to `X` overwritten. This is prevented by wrapping the critical section in `DI` (Disable Interrupts) and `EI` (Enable Interrupts) instructions, making the `X = X - 2` operation atomic.

```assembly
DI                  ; --- Start of ATOMIC operation ---
LD      X           ; Load current X
SUB     CONST_2     ; Calculate X - 2
CALL    CHECK_X_ODZ ; Check/correct value
ST      X           ; Store new X
EI                  ; --- End of ATOMIC operation ---
```

### ODZ Enforcement Subroutine
The `CHECK_X_ODZ` subroutine is a shared utility function called by both the main loop and `INT3_HANDLER`. It takes a value in the accumulator, compares it against `MIN_ODZ_X` and `MAX_ODZ_X`, and if the value is out of bounds, it replaces the value in the accumulator with `MAX_ODZ_X`. This ensures data integrity for the shared variable `X`.

## Data Representation and Valid Domain (ODZ)

-   **Data Representation:**
    -   Variable `X` and constants are 16-bit signed integers (`[-32768, 32767]`).
    -   The value from VU-3's data register is an 8-bit signed integer (`[-128, 127]`).
    -   The output of the function `F(X)` for VU-1 is an 8-bit signed integer (`[-128, 127]`).

-   **Valid Domain (ODZ) for X:**
    The ODZ for `X` is determined by the constraint that the output of `F(X) = 4X - 8` must fit into an 8-bit signed register.
    1.  `-128 <= 4X - 8 <= 127`
    2.  `-120 <= 4X <= 135`
    3.  `-30 <= X <= 33.75`

    Since `X` must be an integer, the final valid domain for `X` is **`[-30, 33]`**. The program enforces this by storing `MIN_ODZ_X = -30` and `MAX_ODZ_X = 33`.

## Memory Map

| Address Range  | Component                    | Description                                  |
|----------------|------------------------------|----------------------------------------------|
| `0x000` - `0x00F` | Interrupt Vector Table (IVT) | Pointers to ISRs for V0-V7.                  |
| `0x020` - `0x031` | Constants                    | `MIN_ODZ_X`, `MAX_ODZ_X`, `CONST_2`, `CONST_8`.|
| `0x033`        | Variable X                   | The shared 16-bit signed integer.            |
| `0x050` - `0x05E` | Main Program & ODZ Check     | The main loop and the `CHECK_X_ODZ` subroutine. |
| `0x060`s (approx) | Interrupt Service Routines   | The code for `INT1_HANDLER` and `INT3_HANDLER`. |

## Assembler Source Code

```assembly
; ============================
; Interrupt Vector Table
; ============================
            ORG     0x000
V0:         WORD    $DEFAULT, 0x180
V1:         WORD    $INT1_HANDLER, 0x180
V2:         WORD    $DEFAULT, 0x180
V3:         WORD    $INT3_HANDLER, 0x180
V4:         WORD    $DEFAULT, 0x180
V5:         WORD    $DEFAULT, 0x180
V6:         WORD    $DEFAULT, 0x180
V7:         WORD    $DEFAULT, 0x180
DEFAULT:    IRET

; ============================
; Constants and Variables
; ============================
            ORG     0x020
MIN_ODZ_X:  WORD    -30
MAX_ODZ_X:  WORD    33
CONST_2:    WORD    2
CONST_8:    WORD    8
TEMP_STORAGE: WORD  0

            ORG     0x33
X:          WORD    0

; ============================
; Main Program
; ============================
            ORG     0x050
START:
            DI
            CLA
            OUT     0x1             ; Disable VU-0
            OUT     0x5             ; Disable VU-2
            ; ... Disable other unused VUs ...
            LD      #0x9            ; Enable Interrupt, Vector #1
            OUT     0x3             ; Write to MR of VU-1
            LD      #0xB            ; Enable Interrupt, Vector #3
            OUT     0x7             ; Write to MR of VU-3
            EI

MAIN_LOOP:
            DI
            LD      X
            SUB     CONST_2
            CALL    CHECK_X_ODZ
            ST      X
            EI
            JUMP    MAIN_LOOP

CHECK_X_ODZ:
            CMP     MIN_ODZ_X
            BMI     LOAD_MAX_VALUE
            CMP     MAX_ODZ_X
            BEQ     ODZ_EXIT
            BPL     LOAD_MAX_VALUE
ODZ_EXIT:
            RET
LOAD_MAX_VALUE:
            LD      MAX_ODZ_X
            RET

; ============================
; Interrupt Service Routines
; ============================
INT1_HANDLER:
            DI
            PUSH
            LD      X
            ASL
            ASL
            SUB     CONST_8
            OUT     0x2
            POP
            EI
            IRET

INT3_HANDLER:
            DI
            PUSH
            IN      0x6
            SXTB
            ST      TEMP_STORAGE
            LD      TEMP_STORAGE
            SUB     X
            CALL    CHECK_X_ODZ
            ST      X
            POP
            EI
            IRET
```

## Conclusion

This laboratory work provided a deep practical understanding of interrupt-driven program control. I learned how to configure and manage interrupts using the Interrupt Vector Table and Management Registers. The project required implementing concurrent processes (a main loop and multiple ISRs) that share data, which highlighted the critical importance of concurrency control. I implemented atomic operations using `DI` and `EI` to prevent race conditions and ensure data integrity. Furthermore, I practiced creating shared subroutines for common logic (like ODZ checking) and handling data of different bit-lengths (8-bit and 16-bit signed numbers), including the use of sign extension.

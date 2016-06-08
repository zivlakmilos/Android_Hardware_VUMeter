/*
 * Code for bluetooth uart and rfid spi modules on avr microcontroller.
 * Copyright (C) 2016 Milos Zivlak <zivlakmilos@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

#include "main.h"

#include <avr/io.h>
#include <avr/interrupt.h>
#include <util/delay.h>

#include "uart.h"

#define BAUD 9600

int main(void)
{
    char byte = 0x00;
    unsigned char i, j;

    DDRB = 0x02;
    DDRC = 0x00;
    DDRD = 0xFC;

    UART_init(BAUD, UART_INTERRUPT_DISABLE);

    DDRB |= (1 << DDB5);

    SREG |= 0x80;                         // Enable global interrupts

    while(1)
    {
        UART_scanf("Akcija: %c", &byte);
        PORTD = 0x00;
        PORTB = 0x00;
        for(i = 0, j = 0; i < byte; i += 8, j++)
        {
            PORTD |= (1 << j);
        }
        PORTB = (PORTD >> 6);
        PORTD = (PORTD << 2);
        PORTD &= 0xFC;
        PORTB &= 0x02;
    }

    return 0;
}

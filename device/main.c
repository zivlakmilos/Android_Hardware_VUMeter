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
    unsigned char i;

    DDRB = 0x02;
    DDRC = 0x00;
    DDRD = 0xFE;

    UART_init(BAUD, UART_INTERRUPT_DISABLE);

    DDRB |= (1 << DDB5);

    SREG |= 0x80;                         // Enable global interrupts

    while(1)
    {
        UART_scanf("Akcija: %c", &byte);
        PORTD = (byte << 2);
        PORTD &= 0xFE;
        _delay_ms(100);
    }

    return 0;
}

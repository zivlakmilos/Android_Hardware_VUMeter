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
#include "uart.h"

#include <avr/io.h>
#include <avr/interrupt.h>
#include <stdarg.h>
#include <stdlib.h>

static volatile char rxData = UART_NODATA;

ISR(USART_RX_vect)
{
    //while(!(UCSR0A & (1 << RXC0)));
    rxData = UDR0;
}

void UART_init(unsigned short baud, unsigned char interrupt)
{
    unsigned short baudRate;
    interrupt &= 0x03;

    baudRate = F_CPU / 16 / baud - 1;
    UBRR0H = (unsigned char) (baudRate >> 8);
    UBRR0L = (unsigned char) baudRate;
    UCSR0B = (1 << RXEN0) | (1 << TXEN0);
    UCSR0C = (1 << UCSZ01) | (1 << UCSZ00);
    UCSR0B |= (interrupt << 5);
    SREG |= 80;
}

void UART_transmit(char data)
{
    while(!(UCSR0A & (1 << UDRE0)));
    UDR0 = data;
}

void UART_transmitInt(int data)
{
    unsigned char n = 1;
    unsigned char i;
    char *zaSlanje;

    int tmp = data;
    while((tmp /= 10) > 0)
        n++;

    zaSlanje = (char *)malloc(n * sizeof(int));
    //char zaSlanje[n];
    for(i = 0; i < n; i++, data /= 10)
        zaSlanje[n - i - 1] = data % 10 + 0x30;
    for(i = 0; i < n; i++)
        UART_transmit(zaSlanje[i]);
    free(zaSlanje);
}

void UART_printf(char *data, ...)
{
    va_list args;
    va_start(args, data);

    do
    {
        if(*data == '%')
        {
            switch(*++data)
            {
                case 'd':
                    UART_transmitInt(va_arg(args, int));
                    break;
                case 'c':
                    UART_transmit((char)(va_arg(args, int)));
                    break;
                case 's':
                    /*
                     * TODO:
                     *  uraditi UART_transmitString(char *);
                     *  i ovde je pozvati
                     */
                    break;
                default:
                    UART_transmit((char)(*data));
                    break;
            }
        } else
        {
            UART_transmit(*data);
        }
    } while(*(++data) != '\0');

    va_end(args);
}

void UART_recive(char *data)
{
    if(!(UCSR0B & (1 << RXCIE0)))
    {
        while(!(UCSR0A & (1 << RXC0)));
        *data = UDR0;
    } else
    {
        SREG &= ~0x80;
        *data = rxData;
        if(rxData != UART_NODATA)
            rxData = UART_NODATA;
        SREG |= 0x80;
    }
}

void UART_scanf(char *data, ...)
{
    va_list args;
    va_start(args, data);

    do
    {
        if(*data == '%')
        {
            switch(*++data)
            {
                case 'c':
                    UART_recive(va_arg(args, char *));
                    break;
                case 's':
                    /*
                     * TODO:
                     *  Uraditi UART_reciveString(char *data);
                     *  i onda i ovde kod koji to poziva
                     */
                    break;
                default:
                    UART_transmit(*data);
                    break;
            }
        } else
            UART_transmit(*data);
    } while(*(++data) != '\0');

    va_end(args);
}

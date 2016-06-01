CC = avr-gcc
CCFLAGS = -mmcu=atmega328p

SRC = \
	  main.c \
	  uart.c

OBJS = $(SRC:.c=.o)

LIBS =

all: $(OBJS)
	$(CC) $(CCFLAGS) -o bluetooth.elf $^ $(LIBS)

%.o: %.c
	$(CC) $(CCFLAGS) -o $@ -c $<

clean:
	rm -Rf *.hex *.elf *.o

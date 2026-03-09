/*     */ package net.minecraft.commands;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class BrigadierExceptions implements BuiltInExceptionProvider {
/*  10 */   private static final Dynamic2CommandExceptionType DOUBLE_TOO_SMALL = new Dynamic2CommandExceptionType((found, min) -> Component.translatableEscape("argument.double.low", new Object[] { min, found }));
/*  11 */   private static final Dynamic2CommandExceptionType DOUBLE_TOO_BIG = new Dynamic2CommandExceptionType((found, max) -> Component.translatableEscape("argument.double.big", new Object[] { max, found }));
/*     */   
/*  13 */   private static final Dynamic2CommandExceptionType FLOAT_TOO_SMALL = new Dynamic2CommandExceptionType((found, min) -> Component.translatableEscape("argument.float.low", new Object[] { min, found }));
/*  14 */   private static final Dynamic2CommandExceptionType FLOAT_TOO_BIG = new Dynamic2CommandExceptionType((found, max) -> Component.translatableEscape("argument.float.big", new Object[] { max, found }));
/*     */   
/*  16 */   private static final Dynamic2CommandExceptionType INTEGER_TOO_SMALL = new Dynamic2CommandExceptionType((found, min) -> Component.translatableEscape("argument.integer.low", new Object[] { min, found }));
/*  17 */   private static final Dynamic2CommandExceptionType INTEGER_TOO_BIG = new Dynamic2CommandExceptionType((found, max) -> Component.translatableEscape("argument.integer.big", new Object[] { max, found }));
/*     */   
/*  19 */   private static final Dynamic2CommandExceptionType LONG_TOO_SMALL = new Dynamic2CommandExceptionType((found, min) -> Component.translatableEscape("argument.long.low", new Object[] { min, found }));
/*  20 */   private static final Dynamic2CommandExceptionType LONG_TOO_BIG = new Dynamic2CommandExceptionType((found, max) -> Component.translatableEscape("argument.long.big", new Object[] { max, found }));
/*     */   
/*  22 */   private static final DynamicCommandExceptionType LITERAL_INCORRECT = new DynamicCommandExceptionType(expected -> Component.translatableEscape("argument.literal.incorrect", new Object[] { expected }));
/*     */   
/*  24 */   private static final SimpleCommandExceptionType READER_EXPECTED_START_OF_QUOTE = new SimpleCommandExceptionType(Component.translatable("parsing.quote.expected.start"));
/*  25 */   private static final SimpleCommandExceptionType READER_EXPECTED_END_OF_QUOTE = new SimpleCommandExceptionType(Component.translatable("parsing.quote.expected.end"));
/*  26 */   private static final DynamicCommandExceptionType READER_INVALID_ESCAPE = new DynamicCommandExceptionType(character -> Component.translatableEscape("parsing.quote.escape", new Object[] { character }));
/*  27 */   private static final DynamicCommandExceptionType READER_INVALID_BOOL = new DynamicCommandExceptionType(value -> Component.translatableEscape("parsing.bool.invalid", new Object[] { value }));
/*  28 */   private static final DynamicCommandExceptionType READER_INVALID_INT = new DynamicCommandExceptionType(value -> Component.translatableEscape("parsing.int.invalid", new Object[] { value }));
/*  29 */   private static final SimpleCommandExceptionType READER_EXPECTED_INT = new SimpleCommandExceptionType(Component.translatable("parsing.int.expected"));
/*  30 */   private static final DynamicCommandExceptionType READER_INVALID_LONG = new DynamicCommandExceptionType(value -> Component.translatableEscape("parsing.long.invalid", new Object[] { value }));
/*  31 */   private static final SimpleCommandExceptionType READER_EXPECTED_LONG = new SimpleCommandExceptionType(Component.translatable("parsing.long.expected"));
/*  32 */   private static final DynamicCommandExceptionType READER_INVALID_DOUBLE = new DynamicCommandExceptionType(value -> Component.translatableEscape("parsing.double.invalid", new Object[] { value }));
/*  33 */   private static final SimpleCommandExceptionType READER_EXPECTED_DOUBLE = new SimpleCommandExceptionType(Component.translatable("parsing.double.expected"));
/*  34 */   private static final DynamicCommandExceptionType READER_INVALID_FLOAT = new DynamicCommandExceptionType(value -> Component.translatableEscape("parsing.float.invalid", new Object[] { value }));
/*  35 */   private static final SimpleCommandExceptionType READER_EXPECTED_FLOAT = new SimpleCommandExceptionType(Component.translatable("parsing.float.expected"));
/*  36 */   private static final SimpleCommandExceptionType READER_EXPECTED_BOOL = new SimpleCommandExceptionType(Component.translatable("parsing.bool.expected"));
/*  37 */   private static final DynamicCommandExceptionType READER_EXPECTED_SYMBOL = new DynamicCommandExceptionType(symbol -> Component.translatableEscape("parsing.expected", new Object[] { symbol }));
/*     */   
/*  39 */   private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_COMMAND = new SimpleCommandExceptionType(Component.translatable("command.unknown.command"));
/*  40 */   private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_ARGUMENT = new SimpleCommandExceptionType(Component.translatable("command.unknown.argument"));
/*  41 */   private static final SimpleCommandExceptionType DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new SimpleCommandExceptionType(Component.translatable("command.expected.separator"));
/*  42 */   private static final DynamicCommandExceptionType DISPATCHER_PARSE_EXCEPTION = new DynamicCommandExceptionType(message -> Component.translatableEscape("command.exception", new Object[] { message }));
/*     */ 
/*     */ 
/*     */   
/*  46 */   public Dynamic2CommandExceptionType doubleTooLow() { return DOUBLE_TOO_SMALL; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public Dynamic2CommandExceptionType doubleTooHigh() { return DOUBLE_TOO_BIG; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public Dynamic2CommandExceptionType floatTooLow() { return FLOAT_TOO_SMALL; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public Dynamic2CommandExceptionType floatTooHigh() { return FLOAT_TOO_BIG; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public Dynamic2CommandExceptionType integerTooLow() { return INTEGER_TOO_SMALL; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public Dynamic2CommandExceptionType integerTooHigh() { return INTEGER_TOO_BIG; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public Dynamic2CommandExceptionType longTooLow() { return LONG_TOO_SMALL; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public Dynamic2CommandExceptionType longTooHigh() { return LONG_TOO_BIG; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public DynamicCommandExceptionType literalIncorrect() { return LITERAL_INCORRECT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public SimpleCommandExceptionType readerExpectedStartOfQuote() { return READER_EXPECTED_START_OF_QUOTE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public SimpleCommandExceptionType readerExpectedEndOfQuote() { return READER_EXPECTED_END_OF_QUOTE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public DynamicCommandExceptionType readerInvalidEscape() { return READER_INVALID_ESCAPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public DynamicCommandExceptionType readerInvalidBool() { return READER_INVALID_BOOL; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public DynamicCommandExceptionType readerInvalidInt() { return READER_INVALID_INT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public SimpleCommandExceptionType readerExpectedInt() { return READER_EXPECTED_INT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public DynamicCommandExceptionType readerInvalidLong() { return READER_INVALID_LONG; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   public SimpleCommandExceptionType readerExpectedLong() { return READER_EXPECTED_LONG; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public DynamicCommandExceptionType readerInvalidDouble() { return READER_INVALID_DOUBLE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   public SimpleCommandExceptionType readerExpectedDouble() { return READER_EXPECTED_DOUBLE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   public DynamicCommandExceptionType readerInvalidFloat() { return READER_INVALID_FLOAT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public SimpleCommandExceptionType readerExpectedFloat() { return READER_EXPECTED_FLOAT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public SimpleCommandExceptionType readerExpectedBool() { return READER_EXPECTED_BOOL; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public DynamicCommandExceptionType readerExpectedSymbol() { return READER_EXPECTED_SYMBOL; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public SimpleCommandExceptionType dispatcherUnknownCommand() { return DISPATCHER_UNKNOWN_COMMAND; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   public SimpleCommandExceptionType dispatcherUnknownArgument() { return DISPATCHER_UNKNOWN_ARGUMENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public SimpleCommandExceptionType dispatcherExpectedArgumentSeparator() { return DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   public DynamicCommandExceptionType dispatcherParseException() { return DISPATCHER_PARSE_EXCEPTION; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\BrigadierExceptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
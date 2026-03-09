/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ public final class WorldCoordinate extends Record {
/*    */   private final boolean relative;
/*    */   private final double value;
/*    */   private static final char PREFIX_RELATIVE = '~';
/*    */   
/*  8 */   public WorldCoordinate(boolean relative, double value) { this.relative = relative; this.value = value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate; } public boolean relative() { return this.relative; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public double value() { return this.value; }
/*    */ 
/*    */   
/* 11 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_DOUBLE = new SimpleCommandExceptionType(Component.translatable("argument.pos.missing.double"));
/* 12 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_INT = new SimpleCommandExceptionType(Component.translatable("argument.pos.missing.int"));
/*    */   
/*    */   public double get(double original) {
/* 15 */     if (this.relative) {
/* 16 */       return this.value + original;
/*    */     }
/* 18 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public static WorldCoordinate parseDouble(StringReader reader, boolean center) throws CommandSyntaxException {
/* 23 */     if (reader.canRead() && reader.peek() == '^') {
/* 24 */       throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(reader);
/*    */     }
/*    */     
/* 27 */     if (!reader.canRead()) {
/* 28 */       throw ERROR_EXPECTED_DOUBLE.createWithContext(reader);
/*    */     }
/*    */     
/* 31 */     boolean relative = isRelative(reader);
/* 32 */     int start = reader.getCursor();
/* 33 */     double value = (reader.canRead() && reader.peek() != ' ') ? reader.readDouble() : 0.0D;
/* 34 */     String number = reader.getString().substring(start, reader.getCursor());
/*    */     
/* 36 */     if (relative && number.isEmpty()) {
/* 37 */       return new WorldCoordinate(true, 0.0D);
/*    */     }
/*    */     
/* 40 */     if (!number.contains(".") && !relative && center) {
/* 41 */       value += 0.5D;
/*    */     }
/*    */     
/* 44 */     return new WorldCoordinate(relative, value);
/*    */   }
/*    */   public static WorldCoordinate parseInt(StringReader reader) throws CommandSyntaxException {
/*    */     double value;
/* 48 */     if (reader.canRead() && reader.peek() == '^') {
/* 49 */       throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(reader);
/*    */     }
/*    */     
/* 52 */     if (!reader.canRead()) {
/* 53 */       throw ERROR_EXPECTED_INT.createWithContext(reader);
/*    */     }
/*    */     
/* 56 */     boolean relative = isRelative(reader);
/*    */     
/* 58 */     if (reader.canRead() && reader.peek() != ' ') {
/* 59 */       value = relative ? reader.readDouble() : reader.readInt();
/*    */     } else {
/* 61 */       value = 0.0D;
/*    */     } 
/* 63 */     return new WorldCoordinate(relative, value);
/*    */   }
/*    */   
/*    */   public static boolean isRelative(StringReader reader) {
/*    */     boolean relative;
/* 68 */     if (reader.peek() == '~') {
/* 69 */       relative = true;
/* 70 */       reader.skip();
/*    */     } else {
/* 72 */       relative = false;
/*    */     } 
/* 74 */     return relative;
/*    */   }
/*    */ 
/*    */   
/* 78 */   public boolean isRelative() { return this.relative; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\coordinates\WorldCoordinate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
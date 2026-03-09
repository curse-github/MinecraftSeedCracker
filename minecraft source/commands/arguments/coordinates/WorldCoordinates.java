/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ 
/*    */ public final class WorldCoordinates extends Record implements Coordinates {
/*    */   private final WorldCoordinate x;
/*    */   private final WorldCoordinate y;
/*    */   private final WorldCoordinate z;
/*    */   
/*  9 */   public WorldCoordinates(WorldCoordinate x, WorldCoordinate y, WorldCoordinate z) { this.x = x; this.y = y; this.z = z; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates; } public WorldCoordinate x() { return this.x; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinates;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public WorldCoordinate y() { return this.y; } public WorldCoordinate z() { return this.z; }
/* 10 */   public static final WorldCoordinates ZERO_ROTATION = absolute(new Vec2(0.0F, 0.0F));
/*    */ 
/*    */   
/*    */   public Vec3 getPosition(CommandSourceStack sender) {
/* 14 */     Vec3 pos = sender.getPosition();
/* 15 */     return new Vec3(this.x.get(pos.x), this.y.get(pos.y), this.z.get(pos.z));
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec2 getRotation(CommandSourceStack sender) {
/* 20 */     Vec2 rot = sender.getRotation();
/* 21 */     return new Vec2((float)this.x.get(rot.x), (float)this.y.get(rot.y));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean isXRelative() { return this.x.isRelative(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public boolean isYRelative() { return this.y.isRelative(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public boolean isZRelative() { return this.z.isRelative(); }
/*    */ 
/*    */   
/*    */   public static WorldCoordinates parseInt(StringReader reader) throws CommandSyntaxException {
/* 40 */     int start = reader.getCursor();
/* 41 */     WorldCoordinate x = WorldCoordinate.parseInt(reader);
/* 42 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 43 */       reader.setCursor(start);
/* 44 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 46 */     reader.skip();
/* 47 */     WorldCoordinate y = WorldCoordinate.parseInt(reader);
/* 48 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 49 */       reader.setCursor(start);
/* 50 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 52 */     reader.skip();
/* 53 */     WorldCoordinate z = WorldCoordinate.parseInt(reader);
/* 54 */     return new WorldCoordinates(x, y, z);
/*    */   }
/*    */   
/*    */   public static WorldCoordinates parseDouble(StringReader reader, boolean centerCorrect) throws CommandSyntaxException {
/* 58 */     int start = reader.getCursor();
/* 59 */     WorldCoordinate x = WorldCoordinate.parseDouble(reader, centerCorrect);
/* 60 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 61 */       reader.setCursor(start);
/* 62 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 64 */     reader.skip();
/* 65 */     WorldCoordinate y = WorldCoordinate.parseDouble(reader, false);
/* 66 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 67 */       reader.setCursor(start);
/* 68 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 70 */     reader.skip();
/* 71 */     WorldCoordinate z = WorldCoordinate.parseDouble(reader, centerCorrect);
/* 72 */     return new WorldCoordinates(x, y, z);
/*    */   }
/*    */ 
/*    */   
/* 76 */   public static WorldCoordinates absolute(double x, double y, double z) { return new WorldCoordinates(new WorldCoordinate(false, x), new WorldCoordinate(false, y), new WorldCoordinate(false, z)); }
/*    */ 
/*    */ 
/*    */   
/* 80 */   public static WorldCoordinates absolute(Vec2 rotation) { return new WorldCoordinates(new WorldCoordinate(false, rotation.x), new WorldCoordinate(false, rotation.y), new WorldCoordinate(true, 0.0D)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\coordinates\WorldCoordinates.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
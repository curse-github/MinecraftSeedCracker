/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ 
/*    */ public final class StructurePieceSerializationContext extends Record {
/*    */   private final ResourceManager resourceManager;
/*    */   private final RegistryAccess registryAccess;
/*    */   private final StructureTemplateManager structureTemplateManager;
/*    */   
/*  9 */   public StructurePieceSerializationContext(ResourceManager resourceManager, RegistryAccess registryAccess, StructureTemplateManager structureTemplateManager) { this.resourceManager = resourceManager; this.registryAccess = registryAccess; this.structureTemplateManager = structureTemplateManager; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext; } public ResourceManager resourceManager() { return this.resourceManager; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public RegistryAccess registryAccess() { return this.registryAccess; } public StructureTemplateManager structureTemplateManager() { return this.structureTemplateManager; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static StructurePieceSerializationContext fromLevel(ServerLevel level) {
/* 15 */     MinecraftServer server = level.getServer();
/* 16 */     return new StructurePieceSerializationContext(server
/* 17 */         .getResourceManager(), server
/* 18 */         .registryAccess(), server
/* 19 */         .getStructureManager());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pieces\StructurePieceSerializationContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
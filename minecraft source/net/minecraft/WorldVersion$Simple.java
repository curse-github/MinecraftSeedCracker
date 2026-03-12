/*    */ package net.minecraft;
/*    */ 
/*    */ import java.util.Date;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*    */ import net.minecraft.world.level.storage.DataVersion;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Simple
/*    */   extends Record
/*    */   implements WorldVersion
/*    */ {
/*    */   private final String id;
/*    */   private final String name;
/*    */   private final DataVersion dataVersion;
/*    */   private final int protocolVersion;
/*    */   private final PackFormat resourcePackVersion;
/*    */   private final PackFormat datapackVersion;
/*    */   private final Date buildTime;
/*    */   private final boolean stable;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/WorldVersion$Simple;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/WorldVersion$Simple; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/WorldVersion$Simple;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/WorldVersion$Simple; }
/*    */   
/* 83 */   public Simple(String id, String name, DataVersion dataVersion, int protocolVersion, PackFormat resourcePackVersion, PackFormat datapackVersion, Date buildTime, boolean stable) { this.id = id; this.name = name; this.dataVersion = dataVersion; this.protocolVersion = protocolVersion; this.resourcePackVersion = resourcePackVersion; this.datapackVersion = datapackVersion; this.buildTime = buildTime; this.stable = stable; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/WorldVersion$Simple;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/WorldVersion$Simple;
/* 83 */     //   0	8	1	o	Ljava/lang/Object; } public String id() { return this.id; } public String name() { return this.name; } public DataVersion dataVersion() { return this.dataVersion; } public int protocolVersion() { return this.protocolVersion; } public PackFormat resourcePackVersion() { return this.resourcePackVersion; } public PackFormat datapackVersion() { return this.datapackVersion; } public Date buildTime() { return this.buildTime; } public boolean stable() { return this.stable; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PackFormat packVersion(PackType packType) {
/* 95 */     switch (WorldVersion.null.$SwitchMap$net$minecraft$server$packs$PackType[packType.ordinal()]) { default: throw new MatchException(null, null);case 1: case 2: break; }  return 
/*    */       
/* 97 */       this.datapackVersion;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\WorldVersion$Simple.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
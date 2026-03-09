/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.repository.PackRepository;
/*    */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*    */ import net.minecraft.server.packs.resources.MultiPackResourceManager;
/*    */ import net.minecraft.world.level.WorldDataConfiguration;
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
/*    */ 
/*    */ 
/*    */ public final class PackConfig
/*    */   extends Record
/*    */ {
/*    */   private final PackRepository packRepository;
/*    */   private final WorldDataConfiguration initialDataConfig;
/*    */   private final boolean safeMode;
/*    */   private final boolean initMode;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$PackConfig;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/WorldLoader$PackConfig; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$PackConfig;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/WorldLoader$PackConfig; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$PackConfig;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/WorldLoader$PackConfig;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 86 */   public PackConfig(PackRepository packRepository, WorldDataConfiguration initialDataConfig, boolean safeMode, boolean initMode) { this.packRepository = packRepository; this.initialDataConfig = initialDataConfig; this.safeMode = safeMode; this.initMode = initMode; } public PackRepository packRepository() { return this.packRepository; } public WorldDataConfiguration initialDataConfig() { return this.initialDataConfig; } public boolean safeMode() { return this.safeMode; } public boolean initMode() { return this.initMode; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Pair<WorldDataConfiguration, CloseableResourceManager> createResourceManager() {
/* 93 */     WorldDataConfiguration newPackConfig = MinecraftServer.configurePackRepository(this.packRepository, this.initialDataConfig, this.initMode, this.safeMode);
/*    */     
/* 95 */     List<PackResources> openedPacks = this.packRepository.openAllSelected();
/* 96 */     MultiPackResourceManager multiPackResourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, openedPacks);
/* 97 */     return Pair.of(newPackConfig, multiPackResourceManager);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\WorldLoader$PackConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
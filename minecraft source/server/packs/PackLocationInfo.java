/*    */ package net.minecraft.server.packs;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ import net.minecraft.server.packs.repository.PackSource;
/*    */ 
/*    */ public final class PackLocationInfo extends Record {
/*    */   private final String id;
/*    */   private final Component title;
/*    */   private final PackSource source;
/*    */   private final Optional<KnownPack> knownPackInfo;
/*    */   
/* 13 */   public PackLocationInfo(String id, Component title, PackSource source, Optional<KnownPack> knownPackInfo) { this.id = id; this.title = title; this.source = source; this.knownPackInfo = knownPackInfo; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/PackLocationInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/server/packs/PackLocationInfo; } public String id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/PackLocationInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/PackLocationInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/PackLocationInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/PackLocationInfo;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public Component title() { return this.title; } public PackSource source() { return this.source; } public Optional<KnownPack> knownPackInfo() { return this.knownPackInfo; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Component createChatLink(boolean enabled, Component description) {
/* 20 */     return ComponentUtils.wrapInSquareBrackets(this.source.decorate(Component.literal(this.id))).withStyle(s -> s
/* 21 */         .withColor(enabled ? ChatFormatting.GREEN : ChatFormatting.RED)
/* 22 */         .withInsertion(StringArgumentType.escapeIfRequired(this.id))
/* 23 */         .withHoverEvent(new HoverEvent.ShowText(Component.empty().append(this.title).append("\n").append(description))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\PackLocationInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
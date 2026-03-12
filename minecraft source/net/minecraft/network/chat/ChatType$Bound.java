/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Bound
/*     */   extends Record
/*     */ {
/*     */   private final Holder<ChatType> chatType;
/*     */   private final Component name;
/*     */   private final Optional<Component> targetName;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ChatType$Bound;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/ChatType$Bound; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ChatType$Bound;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/ChatType$Bound; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ChatType$Bound;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/chat/ChatType$Bound;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  94 */   public Bound(Holder<ChatType> chatType, Component name, Optional<Component> targetName) { this.chatType = chatType; this.name = name; this.targetName = targetName; } public Holder<ChatType> chatType() { return this.chatType; } public Component name() { return this.name; } public Optional<Component> targetName() { return this.targetName; }
/*  95 */   public static final StreamCodec<RegistryFriendlyByteBuf, Bound> STREAM_CODEC = StreamCodec.composite(ChatType.STREAM_CODEC, Bound::chatType, ComponentSerialization.TRUSTED_STREAM_CODEC, Bound::name, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, Bound::targetName, Bound::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   private Bound(Holder<ChatType> chatType, Component name) { this(chatType, name, Optional.empty()); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public Component decorate(Component content) { return ((ChatType)this.chatType.value()).chat().decorate(content, this); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public Component decorateNarration(Component content) { return ((ChatType)this.chatType.value()).narration().decorate(content, this); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public Bound withTargetName(Component targetName) { return new Bound(this.chatType, this.name, Optional.of(targetName)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ChatType$Bound.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
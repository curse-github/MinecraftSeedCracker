/*    */ package net.minecraft.server.dialog.action;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class CustomAll extends Record implements Action {
/*    */   private final Identifier id;
/*    */   private final Optional<CompoundTag> additions;
/*    */   
/* 12 */   public CustomAll(Identifier id, Optional<CompoundTag> additions) { this.id = id; this.additions = additions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/action/CustomAll;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CustomAll; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/action/CustomAll;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CustomAll; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/action/CustomAll;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/action/CustomAll;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<CompoundTag> additions() { return this.additions; }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static final MapCodec<CustomAll> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 17 */         .fieldOf("id").forGetter(CustomAll::id), CompoundTag.CODEC
/* 18 */         .optionalFieldOf("additions").forGetter(CustomAll::additions))
/* 19 */       .apply(i, CustomAll::new));
/*    */ 
/*    */ 
/*    */   
/* 23 */   public MapCodec<CustomAll> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<ClickEvent> createAction(Map<String, Action.ValueGetter> parameters) {
/* 28 */     CompoundTag tag = (CompoundTag)this.additions.map(CompoundTag::copy).orElseGet(CompoundTag::new);
/* 29 */     parameters.forEach((key, value) -> tag.put(key, value.asTag()));
/* 30 */     return Optional.of(new ClickEvent.Custom(this.id, Optional.of(tag)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\action\CustomAll.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
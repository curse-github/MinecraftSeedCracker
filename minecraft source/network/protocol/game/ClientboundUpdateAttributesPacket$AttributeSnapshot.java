/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Collection;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
/*    */ public final class AttributeSnapshot
/*    */   extends Record
/*    */ {
/*    */   private final Holder<Attribute> attribute;
/*    */   private final double base;
/*    */   private final Collection<AttributeModifier> modifiers;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 62 */   public AttributeSnapshot(Holder<Attribute> attribute, double base, Collection<AttributeModifier> modifiers) { this.attribute = attribute; this.base = base; this.modifiers = modifiers; } public Holder<Attribute> attribute() { return this.attribute; } public double base() { return this.base; } public Collection<AttributeModifier> modifiers() { return this.modifiers; }
/* 63 */   public static final StreamCodec<ByteBuf, AttributeModifier> MODIFIER_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, AttributeModifier::id, ByteBufCodecs.DOUBLE, AttributeModifier::amount, AttributeModifier.Operation.STREAM_CODEC, AttributeModifier::operation, AttributeModifier::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public static final StreamCodec<RegistryFriendlyByteBuf, AttributeSnapshot> STREAM_CODEC = StreamCodec.composite(Attribute.STREAM_CODEC, AttributeSnapshot::attribute, ByteBufCodecs.DOUBLE, AttributeSnapshot::base, MODIFIER_STREAM_CODEC
/*    */ 
/*    */       
/* 73 */       .apply(ByteBufCodecs.collection(java.util.ArrayList::new)), AttributeSnapshot::modifiers, AttributeSnapshot::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundUpdateAttributesPacket$AttributeSnapshot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.network.protocol.game;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ 
/*    */ public class ClientboundUpdateAttributesPacket extends Object implements Packet<ClientGamePacketListener> {
/* 21 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateAttributesPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundUpdateAttributesPacket::getEntityId, AttributeSnapshot.STREAM_CODEC
/*    */       
/* 23 */       .apply(ByteBufCodecs.list()), ClientboundUpdateAttributesPacket::getValues, ClientboundUpdateAttributesPacket::new);
/*    */   
/*    */   private final int entityId;
/*    */   
/*    */   private final List<AttributeSnapshot> attributes;
/*    */ 
/*    */   
/*    */   public ClientboundUpdateAttributesPacket(int entityId, Collection<AttributeInstance> values) {
/* 31 */     this.entityId = entityId;
/*    */     
/* 33 */     this.attributes = Lists.newArrayList();
/* 34 */     for (AttributeInstance value : values) {
/* 35 */       this.attributes.add(new AttributeSnapshot(value.getAttribute(), value.getBaseValue(), value.getModifiers()));
/*    */     }
/*    */   }
/*    */   
/*    */   private ClientboundUpdateAttributesPacket(int entityId, List<AttributeSnapshot> attributes) {
/* 40 */     this.entityId = entityId;
/* 41 */     this.attributes = attributes;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public PacketType<ClientboundUpdateAttributesPacket> type() { return GamePacketTypes.CLIENTBOUND_UPDATE_ATTRIBUTES; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public void handle(ClientGamePacketListener listener) { listener.handleUpdateAttributes(this); }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public int getEntityId() { return this.entityId; }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public List<AttributeSnapshot> getValues() { return this.attributes; }
/*    */   public static final class AttributeSnapshot extends Record { private final Holder<Attribute> attribute; private final double base; private final Collection<AttributeModifier> modifiers;
/*    */     
/* 62 */     public AttributeSnapshot(Holder<Attribute> attribute, double base, Collection<AttributeModifier> modifiers) { this.attribute = attribute; this.base = base; this.modifiers = modifiers; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 62 */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot; } public Holder<Attribute> attribute() { return this.attribute; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket$AttributeSnapshot;
/* 62 */       //   0	8	1	o	Ljava/lang/Object; } public double base() { return this.base; } public Collection<AttributeModifier> modifiers() { return this.modifiers; }
/* 63 */     public static final StreamCodec<ByteBuf, AttributeModifier> MODIFIER_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, AttributeModifier::id, ByteBufCodecs.DOUBLE, AttributeModifier::amount, AttributeModifier.Operation.STREAM_CODEC, AttributeModifier::operation, AttributeModifier::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 70 */     public static final StreamCodec<RegistryFriendlyByteBuf, AttributeSnapshot> STREAM_CODEC = StreamCodec.composite(Attribute.STREAM_CODEC, AttributeSnapshot::attribute, ByteBufCodecs.DOUBLE, AttributeSnapshot::base, MODIFIER_STREAM_CODEC
/*    */ 
/*    */         
/* 73 */         .apply(ByteBufCodecs.collection(java.util.ArrayList::new)), AttributeSnapshot::modifiers, AttributeSnapshot::new); }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundUpdateAttributesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.gametest.framework.GameTestInstance;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.block.Rotation;
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
/*     */ public final class Data
/*     */   extends Record
/*     */ {
/*     */   private final Optional<ResourceKey<GameTestInstance>> test;
/*     */   private final Vec3i size;
/*     */   private final Rotation rotation;
/*     */   private final boolean ignoreEntities;
/*     */   private final TestInstanceBlockEntity.Status status;
/*     */   private final Optional<Component> errorMessage;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  93 */   public Data(Optional<ResourceKey<GameTestInstance>> test, Vec3i size, Rotation rotation, boolean ignoreEntities, TestInstanceBlockEntity.Status status, Optional<Component> errorMessage) { this.test = test; this.size = size; this.rotation = rotation; this.ignoreEntities = ignoreEntities; this.status = status; this.errorMessage = errorMessage; } public Optional<ResourceKey<GameTestInstance>> test() { return this.test; } public Vec3i size() { return this.size; } public Rotation rotation() { return this.rotation; } public boolean ignoreEntities() { return this.ignoreEntities; } public TestInstanceBlockEntity.Status status() { return this.status; } public Optional<Component> errorMessage() { return this.errorMessage; }
/*  94 */   public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  95 */         ResourceKey.codec(Registries.TEST_INSTANCE).optionalFieldOf("test").forGetter(Data::test), Vec3i.CODEC
/*  96 */         .fieldOf("size").forGetter(Data::size), Rotation.CODEC
/*  97 */         .fieldOf("rotation").forGetter(Data::rotation), Codec.BOOL
/*  98 */         .fieldOf("ignore_entities").forGetter(Data::ignoreEntities), TestInstanceBlockEntity.Status.CODEC
/*  99 */         .fieldOf("status").forGetter(Data::status), ComponentSerialization.CODEC
/* 100 */         .optionalFieldOf("error_message").forGetter(Data::errorMessage))
/* 101 */       .apply(i, Data::new));
/* 102 */   public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
/* 103 */       ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.TEST_INSTANCE)), Data::test, Vec3i.STREAM_CODEC, Data::size, Rotation.STREAM_CODEC, Data::rotation, ByteBufCodecs.BOOL, Data::ignoreEntities, TestInstanceBlockEntity.Status.STREAM_CODEC, Data::status, 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 108 */       ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC), Data::errorMessage, Data::new);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public Data withSize(Vec3i size) { return new Data(this.test, size, this.rotation, this.ignoreEntities, this.status, this.errorMessage); }
/*     */ 
/*     */ 
/*     */   
/* 117 */   public Data withStatus(TestInstanceBlockEntity.Status status) { return new Data(this.test, this.size, this.rotation, this.ignoreEntities, status, Optional.empty()); }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public Data withError(Component error) { return new Data(this.test, this.size, this.rotation, this.ignoreEntities, TestInstanceBlockEntity.Status.FINISHED, Optional.of(error)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\TestInstanceBlockEntity$Data.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
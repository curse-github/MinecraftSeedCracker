/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ class Container extends SavedData {
/* 19 */   public static final Codec<Container> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 20 */         Codec.unboundedMap(ExtraCodecs.RESOURCE_PATH_CODEC, CompoundTag.CODEC).fieldOf("contents").forGetter(()))
/* 21 */       .apply(i, Container::new));
/*    */   
/*    */   private final Map<String, CompoundTag> storage;
/*    */ 
/*    */   
/* 26 */   private Container(Map<String, CompoundTag> storage) { this.storage = new HashMap(storage); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   private Container() { this(new HashMap()); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static SavedDataType<Container> type(String namespace) { return new SavedDataType(CommandStorage.createId(namespace), Container::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE); }
/*    */ 
/*    */   
/*    */   public CompoundTag get(String id) {
/* 38 */     CompoundTag result = (CompoundTag)this.storage.get(id);
/* 39 */     return (result != null) ? result : new CompoundTag();
/*    */   }
/*    */   
/*    */   public void put(String id, CompoundTag contents) {
/* 43 */     if (contents.isEmpty()) {
/* 44 */       this.storage.remove(id);
/*    */     } else {
/* 46 */       this.storage.put(id, contents);
/*    */     } 
/* 48 */     setDirty();
/*    */   }
/*    */ 
/*    */   
/* 52 */   public Stream<Identifier> getKeys(String namespace) { return this.storage.keySet().stream().map(p -> Identifier.fromNamespaceAndPath(namespace, p)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\CommandStorage$Container.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
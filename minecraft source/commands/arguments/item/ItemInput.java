/*    */ package net.minecraft.commands.arguments.item;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.TypedDataComponent;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemInput {
/* 25 */   private static final Dynamic2CommandExceptionType ERROR_STACK_TOO_BIG = new Dynamic2CommandExceptionType((item, count) -> Component.translatableEscape("arguments.item.overstacked", new Object[] { item, count }));
/*    */   
/*    */   private final Holder<Item> item;
/*    */   private final DataComponentPatch components;
/*    */   
/*    */   public ItemInput(Holder<Item> item, DataComponentPatch components) {
/* 31 */     this.item = item;
/* 32 */     this.components = components;
/*    */   }
/*    */ 
/*    */   
/* 36 */   public Item getItem() { return (Item)this.item.value(); }
/*    */ 
/*    */   
/*    */   public ItemStack createItemStack(int count, boolean checkSize) throws CommandSyntaxException {
/* 40 */     ItemStack result = new ItemStack(this.item, count);
/* 41 */     result.applyComponents(this.components);
/* 42 */     if (checkSize && count > result.getMaxStackSize()) {
/* 43 */       throw ERROR_STACK_TOO_BIG.create(getItemName(), Integer.valueOf(result.getMaxStackSize()));
/*    */     }
/* 45 */     return result;
/*    */   }
/*    */   
/*    */   public String serialize(HolderLookup.Provider registries) {
/* 49 */     StringBuilder result = new StringBuilder(getItemName());
/* 50 */     String serializedComponents = serializeComponents(registries);
/* 51 */     if (!serializedComponents.isEmpty()) {
/* 52 */       result.append('[');
/* 53 */       result.append(serializedComponents);
/* 54 */       result.append(']');
/*    */     } 
/* 56 */     return result.toString();
/*    */   }
/*    */   
/*    */   private String serializeComponents(HolderLookup.Provider registries) {
/* 60 */     RegistryOps registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
/* 61 */     return (String)this.components.entrySet().stream()
/* 62 */       .flatMap(entry -> {
/* 63 */           DataComponentType<?> type = (DataComponentType)entry.getKey();
/* 64 */           Identifier key = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
/* 65 */           if (key == null) {
/* 66 */             return Stream.empty();
/*    */           }
/* 68 */           Optional<?> value = (Optional)entry.getValue();
/* 69 */           if (value.isPresent()) {
/* 70 */             TypedDataComponent<?> typedComponent = TypedDataComponent.createUnchecked(type, value.get());
/* 71 */             return typedComponent.encodeValue(ops).result().stream().map(());
/*    */           } 
/*    */ 
/*    */           
/* 75 */           return Stream.of("!" + key.toString());
/*    */ 
/*    */         
/* 78 */         }).collect(Collectors.joining(String.valueOf(',')));
/*    */   }
/*    */ 
/*    */   
/* 82 */   private String getItemName() { return this.item.unwrapKey().map(ResourceKey::identifier).orElseGet(() -> "unknown[" + String.valueOf(this.item) + "]").toString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\ItemInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
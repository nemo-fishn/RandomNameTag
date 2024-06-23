package net.nemo.random_name_tag.mixin;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NameTagItem;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.nemo.random_name_tag.ApiResponse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

@Mixin(NameTagItem.class)
public class RandomNameTagMixin {
    @Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
    private void injected(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) throws IOException {
        if (!user.getWorld().isClient && !(entity instanceof PlayerEntity)) {
            if (stack.getName().getString().equals("random") && entity.isAlive()) {

                URL urlObj = new URL("https://randomuser.me/api/");
                HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    StringBuilder sb = new StringBuilder();
                    Scanner scanner = new Scanner(connection.getInputStream());

                    while (scanner.hasNext()) {
                        sb.append(scanner.nextLine());
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    ApiResponse apiResponse = objectMapper.readValue(String.valueOf(sb), ApiResponse.class);
                    String fullName = apiResponse.getResults().get(0).getName().getTitle()
                            + " " + apiResponse.getResults().get(0).getName().getFirst()
                            + " " + apiResponse.getResults().get(0).getName().getLast();

                    entity.setCustomName(Text.of(fullName));
                    if (entity instanceof MobEntity) {
                        ((MobEntity) entity).setPersistent();
                    }
                    stack.decrement(1);

                    cir.setReturnValue(ActionResult.SUCCESS);
                    cir.cancel();
                } else {
                    System.out.println(responseCode);
                }

            }

        }
    }
}
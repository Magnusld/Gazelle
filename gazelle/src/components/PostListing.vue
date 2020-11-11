<template>
  <div>
    <md-divider/>
    <div class="courseListing">
      <div class="header">
        <div class="horizontalSeparator">
          <a style="color:black" class="title">{{post.name}}</a>
          <md-menu md-direction="bottom-start">
            <md-button md-menu-trigger class="md-icon-button">
              <md-icon>more_horiz</md-icon>
              <md-menu-content class="menu">
                <md-menu-item>
                  <md-button>Rediger</md-button>
                </md-menu-item>
                <md-menu-item>
                  <md-button class="md-accent">Slett</md-button>
                </md-menu-item>
              </md-menu-content>
            </md-button>
          </md-menu>
        </div>
      </div>
      <div>
        <span class="md-body-1">
          Dette er en generic post i faget. Hei på dere! Lykke til på eksamen.
        </span>
      </div>
      <div class="horizontalSeparator">
        <div>Gjort: 3/5</div>
      </div>
      <md-progress-bar
          md-mode="determinate"
          :md-value="100*3/5"
      ></md-progress-bar>
    </div>
  </div>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import {Post} from "@/types";

@Component
export default class PostListing extends Vue{
  @Prop()
  private post!: Post;
}
</script>

<style scoped lang="scss">
.horizontalSeparator{
  display: flex;
  align-content: center;
  align-items: flex-end;
  justify-content: space-between;
}
.menu{
  display: flex;
  align-content: center;
  align-items: baseline;
}
.title{
  font-size: 20px;
  font-weight: 500;
  letter-spacing: .005em;
  line-height: 26px;
  cursor: pointer;
}

</style>
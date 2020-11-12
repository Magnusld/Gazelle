<template>
  <div class="errorPage">
    <md-card>
      <md-card-media-cover>
        <md-card-media>
          <img
            src="../assets/images/ErrorImg.jpg"
            alt="ErrorImg"
            class="image"
          />
        </md-card-media>
        <md-card-area>
          <span class="imgText md-display-3">
            404 Page not found
          </span>
        </md-card-area>
      </md-card-media-cover>
    </md-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class ErrorPage extends Vue {}
</script>

<style scoped lang="scss">
.errorPage {
  padding-top: 10px;
  display: flex;
  justify-content: center;
}

.imgText {
  float: left;
}
.md-card {
  height: 650px;
  margin: 4px;
  display: inline-block;
  vertical-align: top;
}
</style>

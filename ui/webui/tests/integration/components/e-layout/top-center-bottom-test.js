import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-layout/top-center-bottom', 'Integration | Component | e layout/top center bottom', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-layout/top-center-bottom}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-layout/top-center-bottom}}
      template block text
    {{/e-layout/top-center-bottom}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
